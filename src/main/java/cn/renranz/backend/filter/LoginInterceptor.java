package cn.renranz.backend.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.jwt.JWTUtil;
import cn.renranz.backend.constant.consist.JwtConstant;
import cn.renranz.backend.constant.enums.ResponseCodes;
import cn.renranz.backend.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String token = request.getHeader(Header.AUTHORIZATION.getValue());
        if (StrUtil.isEmpty(token)) {
            throw new BusinessException(ResponseCodes.TOKEN_EXPIRE, "请先登录");
        }
        if (!JWTUtil.verify(request.getHeader(Header.AUTHORIZATION.getValue()), JwtConstant.JWT_KEY.getBytes())) {
            throw new BusinessException(ResponseCodes.TOKEN_EXPIRE, "登录过期，请重新登录");
        }
        return true;
    }
}
