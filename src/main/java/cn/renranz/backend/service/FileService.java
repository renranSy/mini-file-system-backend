package cn.renranz.backend.service;

import cn.renranz.backend.model.domain.File;
import cn.renranz.backend.model.dto.DeleteFileReq;
import cn.renranz.backend.model.dto.QueryFileResponse;
import cn.renranz.backend.utils.BaseResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sunyang
 * @description 针对表【file】的数据库操作Service
 * @createDate 2025-05-20 21:19:14
 */
public interface FileService extends IService<File> {

    BaseResponse<?> upload(MultipartFile file, Integer userId);

    BaseResponse<QueryFileResponse> queryFiles(String userId);

    ResponseEntity<InputStreamResource> download(String id, String userId);

    BaseResponse<?> deleteFile(DeleteFileReq data, String userId);
}
