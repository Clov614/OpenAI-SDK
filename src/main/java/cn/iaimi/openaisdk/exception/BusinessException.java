package cn.iaimi.openaisdk.exception;

import cn.iaimi.openaisdk.common.ErrorCode;
import cn.iaimi.openaisdk.model.dto.ai.CreateChatCompletionResponse;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, CreateChatCompletionResponse.ErrorBean errorBean) {
        super(errorBean.getCode());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
