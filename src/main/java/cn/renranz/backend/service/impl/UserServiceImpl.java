package cn.renranz.backend.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.renranz.backend.constant.enums.ResponseCodes;
import cn.renranz.backend.mapper.UserMapper;
import cn.renranz.backend.model.domain.User;
import cn.renranz.backend.model.dto.LoginRequest;
import cn.renranz.backend.model.dto.LoginResponse;
import cn.renranz.backend.service.UserService;
import cn.renranz.backend.utils.BaseResponse;
import cn.renranz.backend.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author sunyang
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-05-20 13:59:34
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        // 根据用户名查询用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));

        if (user == null) {
            return BaseResponse.fail(ResponseCodes.NOT_FOUND, "用户不存在");
        }

        // 验证密码
        if (ObjUtil.notEqual(request.getPassword(), user.getPasswordHash())) {
            return BaseResponse.fail(ResponseCodes.BAD_REQUEST, "用户名或密码错误");
        }

        // 检查用户状态
        if (ObjUtil.equal(user.getEnabled(), 0)) {
            return BaseResponse.fail(ResponseCodes.NO_AUTH, "用户已禁用");
        }

        // 生成JWT token
        Map<String, String> jwtToken = JwtUtil.generateJwtToken(user.getId().toString());

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken.get("token"));
        response.setUsername(user.getUsername());
        response.setUserId(user.getId());

        return BaseResponse.success(response);
    }
}




