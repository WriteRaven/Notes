package com.writeraven.jupiter.mvc.server.exception;

import com.writeraven.jupiter.mvc.server.enums.StatusEnum;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/25 15:26
 * @since JDK 1.8
 */
public class JupiterException extends GenericException {


    public JupiterException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public JupiterException(java.lang.Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public JupiterException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public JupiterException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public JupiterException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public JupiterException(java.lang.Exception oriEx) {
        super(oriEx);
    }

    public JupiterException(Throwable oriEx) {
        super(oriEx);
    }

    public JupiterException(String message, java.lang.Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public JupiterException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }

}
