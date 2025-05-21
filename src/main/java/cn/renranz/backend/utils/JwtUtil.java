package cn.renranz.backend.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.renranz.backend.constant.consist.JwtConstant;
import cn.renranz.backend.constant.enums.ResponseCodes;
import cn.renranz.backend.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class JwtUtil {
    public static Map<String, String> generateJwtToken(String userId) {
        Map<String, Object> payload = Maps.newHashMapWithExpectedSize(6);
        DateTime dateTime = DateTime.now();
        DateTime offsetDatetime = dateTime.offset(DateField.DAY_OF_MONTH, 14);
        payload.put(JWT.ISSUED_AT, dateTime);
        payload.put(JWT.EXPIRES_AT, offsetDatetime);
        payload.put(JWT.NOT_BEFORE, dateTime);
        payload.put("userId", userId);

        Map<String, String> tokenMap = Maps.newHashMapWithExpectedSize(1);
        tokenMap.put("token", JWTUtil.createToken(payload, JwtConstant.JWT_KEY.getBytes()));
        return tokenMap;
    }

    public static Map<String, String> refreshJwtToken(HttpServletRequest request) {
        String oldJwtToken = request.getHeader("Cookie");
        if (ObjUtil.isEmpty(oldJwtToken)) {
            throw new BusinessException(ResponseCodes.TOKEN_EXPIRE, "token无效");
        }

        boolean verify = JWTUtil.verify(oldJwtToken, JwtConstant.JWT_KEY.getBytes());
        if (!verify) {
            throw new BusinessException(ResponseCodes.TOKEN_EXPIRE);
        }

        String userId = JWTUtil.parseToken(oldJwtToken).getPayload("userId").toString();
        return generateJwtToken(userId);
    }

    public static String getUserId(String token) {
        return JWTUtil.parseToken(token).getPayload("userId").toString();
    }
}
