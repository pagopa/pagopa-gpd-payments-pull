package it.gov.pagopa.gpd.payments.pull.models;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
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

    private String nav;
    private String iuv;
    private String paTaxCode;
    private String paFullName;
    private BigDecimal amount;
    private String description;
    private Boolean isPartialPayment;
    private LocalDate dueDate;
    private LocalDate retentionDate;
    private LocalDate insertedDate;
    private BigDecimal notificationFee;
    private String status;
    private LocalDate lastUpdatedDate;

}
