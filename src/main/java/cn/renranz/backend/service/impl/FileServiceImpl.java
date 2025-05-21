package cn.renranz.backend.service.impl;

import cn.hutool.core.lang.UUID;
import cn.renranz.backend.constant.consist.FileConstant;
import cn.renranz.backend.constant.enums.ResponseCodes;
import cn.renranz.backend.exception.BusinessException;
import cn.renranz.backend.mapper.FileMapper;
import cn.renranz.backend.model.domain.File;
import cn.renranz.backend.model.dto.DeleteFileReq;
import cn.renranz.backend.model.dto.QueryFileResponse;
import cn.renranz.backend.service.FileService;
import cn.renranz.backend.utils.AesUtil;
import cn.renranz.backend.utils.BaseResponse;
import cn.renranz.backend.utils.HashUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * @author sunyang
 * @description 针对表【file】的数据库操作Service实现
 * @createDate 2025-05-20 21:19:14
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
        implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Override
    public BaseResponse<?> upload(MultipartFile file, Integer userId) {
        Path uploadPath = Paths.get(FileConstant.UPLOAD_PATH);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String filename = file.getOriginalFilename();
        String filetype = file.getContentType();
        long size = file.getSize();

        String uuid = UUID.randomUUID().toString();

        String uniqueFileName = this.getFullFileName(uuid, filename);
        // 写入文件
        Path filePath = uploadPath.resolve(uniqueFileName);
        try {
            // 创建临时文件
            Path tempPath = Files.createTempFile("upload_", "_temp");
            file.transferTo(tempPath.toFile());

            // 计算文件哈希值
            String fileHash = HashUtil.hashFile(tempPath.toFile());

            // 加密文件
            AesUtil.encryptFile(tempPath.toFile(), filePath.toFile());

            // 删除临时文件
            Files.delete(tempPath);

            File insertFile = new File();
            insertFile.setFilename(filename);
            insertFile.setFiletype(filetype);
            insertFile.setUuid(uuid);
            insertFile.setFilesize(size);
            insertFile.setUserId(userId);
            insertFile.setHash(fileHash);

            int insert = fileMapper.insert(insertFile);

            if (insert == 0) {
                return BaseResponse.fail(ResponseCodes.INSERT_ERROR, "上传失败");
            }
            return BaseResponse.success();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BaseResponse<QueryFileResponse> queryFiles(String userId) {
        // 使用 LambdaQueryWrapper 构建查询条件
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getUserId, userId)
                   .orderByDesc(File::getCreatedAt);
        
        List<File> files = fileMapper.selectList(queryWrapper);

        QueryFileResponse result = new QueryFileResponse();
        result.setList(files);
        return BaseResponse.success(result);
    }

    @Override
    public ResponseEntity<InputStreamResource> download(String id, String userId) {
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("user_id", userId);
        queryMap.put("id", id);
        List<File> files = fileMapper.selectByMap(queryMap);

        if (files == null || files.isEmpty()) {
            throw new BusinessException(ResponseCodes.NOT_FOUND);
        }

        File file = files.get(0);

        String fullFileName = getFullFileName(file.getUuid(), file.getFilename());

        Path path = Paths.get(FileConstant.UPLOAD_PATH).resolve(fullFileName).normalize();
        if (!Files.exists(path)) {
            throw new BusinessException(ResponseCodes.NOT_FOUND);
        }

        if (!path.startsWith(FileConstant.UPLOAD_PATH)) {
            throw new BusinessException(ResponseCodes.NO_AUTH);
        }

        try {
            // 创建临时文件用于解密
            Path tempPath = Files.createTempFile("download_", "_temp");
            // 解密文件
            AesUtil.decryptFile(path.toFile(), tempPath.toFile());

            // 读取解密后的文件内容
            byte[] decryptedContent = Files.readAllBytes(tempPath);
            InputStream inputStream = new ByteArrayInputStream(decryptedContent);

            // 删除临时文件
            Files.delete(tempPath);

            // 获取文件类型（MIME）
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(decryptedContent.length)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            throw new BusinessException(ResponseCodes.SYSTEM_ERROR);
        }
    }

    @Override
    public BaseResponse<?> deleteFile(DeleteFileReq data, String userId) {
        // 验证文件是否存在且属于该用户
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("user_id", userId);
        queryMap.put("id", data.getId());
        List<File> files = fileMapper.selectByMap(queryMap);

        if (files == null || files.isEmpty()) {
            return BaseResponse.fail(ResponseCodes.NOT_FOUND, "文件不存在");
        }

        File file = files.get(0);

        // 删除物理文件
        String fullFileName = getFullFileName(file.getUuid(), file.getFilename());
        Path path = Paths.get(FileConstant.UPLOAD_PATH).resolve(fullFileName).normalize();

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            return BaseResponse.fail(ResponseCodes.SYSTEM_ERROR, "删除文件失败");
        }

        // 删除数据库记录
        int delete = fileMapper.deleteById(data.getId());
        if (delete == 0) {
            return BaseResponse.fail(ResponseCodes.DELETE_ERROR, "删除文件记录失败");
        }

        return BaseResponse.success();
    }

    private String getFullFileName(String uuid, String filename) {
        return uuid + "_" + filename;
    }
}




