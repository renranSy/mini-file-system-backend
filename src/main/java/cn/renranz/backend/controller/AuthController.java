package cn.renranz.backend.controller;

import cn.renranz.backend.model.dto.LoginRequest;
import cn.renranz.backend.model.dto.LoginResponse;
import cn.renranz.backend.service.UserService;
import cn.renranz.backend.utils.BaseResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}