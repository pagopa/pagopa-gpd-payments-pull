package it.gov.pagopa.gpd.payments.pull.exception;

import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;

/**
 * Thrown in case the request do not have the tax code header
 */
public class InvalidTaxCodeHeaderException extends PaymentNoticeException {

    /**
     * Constructs new exception with provided error code and message
     *
     * @param errorCode Error code
     * @param message   Detail message
     */
    public InvalidTaxCodeHeaderException(AppErrorCodeEnum errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructs new exception with provided error code, message and cause
     *
     * @param errorCode Error code
     * @param message   Detail message
     * @param cause     Exception causing the constructed one
     */
    public InvalidTaxCodeHeaderException(AppErrorCodeEnum errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}