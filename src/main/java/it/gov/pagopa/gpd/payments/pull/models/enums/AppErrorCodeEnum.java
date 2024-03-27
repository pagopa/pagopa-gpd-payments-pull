
package it.gov.pagopa.gpd.payments.pull.models.enums;

import lombok.Getter;

/**
 * Enumeration for application error codes and messages
 */
@Getter
public enum AppErrorCodeEnum {

    PPL_600("PPL_600", "Error for invalid request parameters" ),
    PPL_601("PPL_601", "Invalid fiscal code provided on payment notice request" ),

    PPL_700("PPL_700", "Error recovering payment positions from DB"),
    PPL_800("PPL_800", "Error mapping response using recovered data from DB"),
    PPL_900("PPL_900", "Unexpected error on payment pull services");

    private final String errorCode;
    private final String errorMessage;

    AppErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
