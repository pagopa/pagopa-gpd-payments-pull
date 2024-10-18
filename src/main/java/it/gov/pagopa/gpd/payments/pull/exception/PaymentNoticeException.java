package it.gov.pagopa.gpd.payments.pull.exception;

import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import lombok.Getter;

import jakarta.ws.rs.core.Response;
import java.util.Objects;

/**
 * Base exception for PDF Engine exceptions
 */
@Getter
public class PaymentNoticeException extends RuntimeException {

    /**
     * Error code of this exception
     * -- GETTER --
     * Returns error code
     *
     * @return Error code of this exception
     */
    private final AppErrorCodeEnum errorCode;

    /**
     * Constructs new exception with provided error code and message
     *
     * @param errorCode Error code
     * @param message   Detail message
     */
    public PaymentNoticeException(AppErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    /**
     * Constructs new exception with provided error code, message and cause
     *
     * @param errorCode Error code
     * @param message   Detail message
     * @param cause     Exception causing the constructed one
     */
    public PaymentNoticeException(AppErrorCodeEnum errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    public static Response.Status getHttpStatus(PaymentNoticeException e) {
        return e.getErrorCode().getStatus();
    }

}
