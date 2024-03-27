package it.gov.pagopa.gpd.payments.pull.exception;

import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import org.jboss.resteasy.reactive.ResponseStatus;

import javax.ws.rs.core.Response;
import java.util.Objects;

import static org.jboss.resteasy.reactive.RestResponse.StatusCode.BAD_REQUEST;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.INTERNAL_SERVER_ERROR;

/**
 * Base exception for PDF Engine exceptions
 */
public class PaymentNoticeException extends RuntimeException {

    /** Error code of this exception */
    private final AppErrorCodeEnum errorCode;

    /**
     * Constructs new exception with provided error code and message
     *
     * @param errorCode Error code
     * @param message Detail message
     */
    public PaymentNoticeException(AppErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    /**
     * Constructs new exception with provided error code, message and cause
     *
     * @param errorCode Error code
     * @param message Detail message
     * @param cause Exception causing the constructed one
     */
    public PaymentNoticeException(AppErrorCodeEnum errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = Objects.requireNonNull(errorCode);
    }

    public static Response.Status getHttpStatus(PaymentNoticeException e) {
        Response.Status status;
        if (e.getErrorCode().equals(AppErrorCodeEnum.PPL_900) ||
            e.getErrorCode().equals(AppErrorCodeEnum.PPL_700)) {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.BAD_REQUEST;
        }
        return status;
    }

    /**
     * Returns error code
     *
     * @return Error code of this exception
     */
    public AppErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
