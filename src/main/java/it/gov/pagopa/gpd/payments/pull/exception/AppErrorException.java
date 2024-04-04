package it.gov.pagopa.gpd.payments.pull.exception;

import lombok.Getter;

/**
 * Base exception for PDF Engine exceptions
 */
@Getter
public class AppErrorException extends RuntimeException {


    public AppErrorException(Throwable error) {
        super(error);
    }
}
