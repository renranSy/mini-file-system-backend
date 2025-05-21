package cn.renranz.backend.service;

import cn.renranz.backend.model.domain.User;
import cn.renranz.backend.model.dto.LoginRequest;
import cn.renranz.backend.model.dto.LoginResponse;
import cn.renranz.backend.utils.BaseResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sunyang
* @description 针对表【user】的数据库操作Service
* @createDate 2025-05-20 13:59:34
*/
public interface UserService extends IService<User> {
    BaseResponse<LoginResponse> login(LoginRequest request);
}
