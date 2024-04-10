package it.gov.pagopa.gpd.payments.pull.models.enums;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * Enumeration for application error codes and messages
 */
@Getter
public enum AppErrorCodeEnum {

    PPL_600(Response.Status.BAD_REQUEST, "PPL_600", "Error for invalid request parameters"),
    PPL_601(Response.Status.BAD_REQUEST, "PPL_601", "Invalid fiscal code provided on payment notice request"),

    PPL_700(Response.Status.INTERNAL_SERVER_ERROR, "PPL_700", "Error recovering payment positions from DB"),
    PPL_800(Response.Status.BAD_REQUEST, "PPL_800", "Error mapping response using recovered data from DB"),
    PPL_900(Response.Status.INTERNAL_SERVER_ERROR, "PPL_900", "Unexpected error on payment pull services");

    private final Response.Status status;
    private final String errorCode;
    private final String errorMessage;

    AppErrorCodeEnum(Response.Status status, String errorCode, String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
