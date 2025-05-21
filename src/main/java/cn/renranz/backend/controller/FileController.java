package cn.renranz.backend.controller;

import cn.hutool.http.Header;
import cn.renranz.backend.model.dto.DeleteFileReq;
import cn.renranz.backend.model.dto.QueryFileResponse;
import cn.renranz.backend.service.FileService;
import cn.renranz.backend.utils.BaseResponse;
import cn.renranz.backend.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    @GetMapping("query")
    public BaseResponse<QueryFileResponse> queryFiles(HttpServletRequest request) {
        String userId = JwtUtil.getUserId(request.getHeader(Header.AUTHORIZATION.getValue()));

        return fileService.queryFiles(userId);
    }

    @PostMapping("upload")
    public BaseResponse<?> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String userId = JwtUtil.getUserId(request.getHeader(Header.AUTHORIZATION.getValue()));
        return fileService.upload(file, Integer.valueOf(userId));
    }

    @GetMapping("download")
    public ResponseEntity<InputStreamResource> download(@RequestParam("id") String id, HttpServletRequest request) {
        String userId = JwtUtil.getUserId(request.getHeader(Header.AUTHORIZATION.getValue()));
        return fileService.download(id, userId);
    }

    @PostMapping("delete")
    public BaseResponse<?> deleteFile(@RequestBody DeleteFileReq data, HttpServletRequest request) {
        String userId = JwtUtil.getUserId(request.getHeader(Header.AUTHORIZATION.getValue()));
        return fileService.deleteFile(data, userId);
    }
}