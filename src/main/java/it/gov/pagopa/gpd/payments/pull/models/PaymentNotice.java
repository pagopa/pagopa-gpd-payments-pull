package it.gov.pagopa.gpd.payments.pull.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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

    private String iupd;
    private String debtorTaxCode;
    private String debtorFullName;
    private String debtorType;
    private String prTaxCode;
    private String paFullName;
    private LocalDate insertedDate;
    private LocalDate publishDate;
    private LocalDate validityDate;
    private PaymentNoticeStatus status;
    private LocalDate lastUpdateDate;
    private List<PaymentOption> paymentOptions;

}
