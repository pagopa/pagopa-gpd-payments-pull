package it.gov.pagopa.gpd.payments.pull.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
@RegisterForReflection
public class Installment implements Serializable {

    @Schema(description = "Notice Code", required = true)
    private String nav;
    @Schema(description = "Unique identifier", required = true)
    private String iuv;
    @Schema(description = "Tax code of the Creditor Body", required = true)
    private String paTaxCode;
    @Schema(description = "Company name of the Creditor Body", required = true)
    private String paFullName;
    @Schema(description = "Payment Option Amount", required = true)
    private BigDecimal amount;
    @Schema(description = "Description of the OP (e.g. \"SISA - 741T, 942T - Checks without authorization or funding\")",
            required = true)
    private String description;
    @Schema(description = "Indicates whether the OP is part of an installment plan")
    private Boolean isPartialPayment;
    @Schema(description = "Is the date by which (TO) the Payment option is payable.", required = true)
    private LocalDate dueDate;
    @Schema(description = "Not currently used in any logic. The purpose of this date will be to give the possibility" +
            " to specify a time period after the dueDate within which a payment option, even if expired, will still be payable.")
    private LocalDate retentionDate;
    @Schema(description = "date of insertion of the OP", required = true)
    private LocalDate insertedDate;
    @Schema(description = "corresponds to the SEND notification costs")
    private BigDecimal notificationFee;
    @Schema(description = "Status of the OP", required = true)
    private String status;
    @Schema(description = "OP update date", required = true)
    private LocalDate lastUpdatedDate;

}