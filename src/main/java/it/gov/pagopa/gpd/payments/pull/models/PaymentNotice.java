package it.gov.pagopa.gpd.payments.pull.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
@RegisterForReflection
public class PaymentNotice implements Serializable {

    @Schema(description = "Unique ID of the Debt Position (for positions originating from ACA the IUPD starts with ACA_)",
            required = true)
    private String iupd;
    @Schema(description = "Tax code of the person to whom the Debt Position is registered", required = true)
    private String debtorTaxCode;
    @Schema(description = "Full name of the person to whom the Debt Position is registered", required = true)
    private String debtorFullName;
    @Schema(description = "Type of subject to whom the Debt Position is registered (Physical or Legal)", required = true)
    private String debtorType;
    @Schema(description =  "Tax code of the Creditor Body", required = true)
    private String prTaxCode;
    @Schema(description = "Company name of the Creditor Body", required = true)
    private String paFullName;
    @Schema(description = "Date of entry of the Debt Position", required = true)
    private LocalDate insertedDate;
    @Schema(description = "Date of publication of the Debt Position. " +
            "In the case of Positions created by ACA it corresponds to the insertion date.",
            required = true)
    private LocalDate publishDate;
    @Schema(description = "Start date of validity of the Debt Position. " +
            "if set to null it goes directly to valid when publishing",
            required = true)
    private LocalDate validityDate;
    @Schema(description = "State of the Debt Position. Will be\n" +
            "VALID\n" +
            " or\n" +
            "PARTIALLY_PAID",
            required = true)
    private PaymentNoticeStatus status;
    @Schema(description = "Date of update of the Debt Position", required = true)
    private LocalDate lastUpdateDate;
    @Schema(description = "Array structure containing any payment options (there always exists at least 1)",
            required = true)
    private List<PaymentOption> paymentOptions;

}