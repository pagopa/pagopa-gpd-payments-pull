package it.gov.pagopa.gpd.payments.pull.exception;

import lombok.Getter;

import java.time.LocalDate;

/**
 * Base exception for PDF Engine exceptions
 */
@Getter
public class AppErrorException extends RuntimeException {

    private String taxCode;
    private LocalDate dueDate;


    public AppErrorException(Throwable error) {
        super(error);
    }

    public AppErrorException(Throwable cause, LocalDate dueDate, String taxCode) {
        super(cause);
        this.dueDate = dueDate;
        this.taxCode = taxCode;
    }
}
