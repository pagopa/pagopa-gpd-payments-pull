package it.gov.pagopa.gpd.payments.pull.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@RegisterForReflection
public class PaymentOption implements Serializable {

    @Schema(description = "Description of the payment option")
    private String description;

    @Schema(description = "Number of installments that make up the payment option, " +
            "if equal to 1 it is a single payment",
            required = true)
    private Integer numberOfInstallments;

    @Schema(description = "total amount for the payment option")
    private long amount;

    @Schema(description = "Is the date by which the Payment option is payable.")
    private LocalDateTime dueDate;

    @Schema(description = "Indicates whether the OP is part of an installment plan")
    private Boolean isPartialPayment;

    @Schema(description = "Indicates, if set to true, in the case of PD created on GPD," +
            " that once the expiration date (dueDate ) has passed the PD is automatically set to the expired status",
            required = true )
    private Boolean switchToExpired;

    @Schema(description = "Array structure containing the installments that make up " +
            "the payment option (there always exists at least 1)", required = true)
    private List<Installment> installments;

}
