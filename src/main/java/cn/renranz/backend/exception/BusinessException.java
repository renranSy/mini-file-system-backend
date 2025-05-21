package cn.renranz.backend.exception;

import cn.renranz.backend.constant.enums.ResponseCodes;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessException extends RuntimeException {
    private final ResponseCodes responseCodes;

    private final String message;

    public BusinessException(ResponseCodes responseCodes, String message) {
        this.responseCodes = responseCodes;
        this.message = message;
    }

    public BusinessException(ResponseCodes responseCodes) {
        this.responseCodes = responseCodes;
        this.message = responseCodes.getMessage();
    }
}
