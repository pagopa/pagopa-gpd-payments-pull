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
public class PaymentOption implements Serializable {

    private String description;

    private Integer numberOfInstallments;

    private LocalDate dueDate;

    private Boolean switchToExpired;

    private List<Installment> installments;

}
