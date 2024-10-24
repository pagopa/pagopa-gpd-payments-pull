package it.gov.pagopa.gpd.payments.pull.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.gov.pagopa.gpd.payments.pull.models.enums.TransferStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transfer", schema = "apd")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@transferId")
public class Transfer implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -886970813082991109L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "organization_fiscal_code")
    private String organizationFiscalCode;
    @NotNull
    @Column(name = "transfer_id")
    private String idTransfer;
    @NotNull
    private String iuv;
    @NotNull
    private long amount;
    @NotNull
    @Column(name = "remittance_information")
    private String remittanceInformation; // causale
    @NotNull
    private String category; // taxonomy

    private String iban;

    @Column(name = "postal_iban")
    private String postalIban;

    @Column(name = "hash_document")
    private String hashDocument;

    @Column(name = "stamp_type")
    private String stampType;

    @Column(name = "provincial_residence")
    private String provincialResidence;

    @NotNull
    @Column(name = "inserted_date")
    private LocalDateTime insertedDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransferStatus status;
    @NotNull
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;


    @ManyToOne(targetEntity = PaymentOption.class, fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "payment_option_id")
    private PaymentOption paymentOption;

    @Builder.Default
    @OneToMany(
            targetEntity = TransferMetadata.class,
            fetch = FetchType.LAZY,
            mappedBy = "transfer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TransferMetadata> transferMetadata = new ArrayList<>();


}
