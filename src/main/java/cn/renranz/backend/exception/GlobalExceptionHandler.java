package cn.renranz.backend.exception;

import cn.renranz.backend.constant.enums.ResponseCodes;
import cn.renranz.backend.utils.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> businessExceptionHandler(BusinessException e) {
        log.error("businessException: {}", e.getMessage());
        return BaseResponse.fail(e.getResponseCodes(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> runtimeExceptionHandler(RuntimeException e) {
        log.info("runtimeException ", e);
        return BaseResponse.fail(ResponseCodes.SYSTEM_ERROR);
    }
}