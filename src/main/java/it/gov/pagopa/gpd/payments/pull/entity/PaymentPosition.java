package it.gov.pagopa.gpd.payments.pull.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import it.gov.pagopa.gpd.payments.pull.models.enums.DebtPositionStatus;
import it.gov.pagopa.gpd.payments.pull.models.enums.ServiceType;
import it.gov.pagopa.gpd.payments.pull.models.enums.Type;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = "payment_position", schema = "apd")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@paymentPositionId")
public class PaymentPosition implements Serializable {


    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -8637183968286214359L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_POS_SEQ")
    @SequenceGenerator(name = "PAYMENT_POS_SEQ", sequenceName = "PAYMENT_POS_SEQ", allocationSize = 1)
    private Long id;

    @NotNull
    private String iupd;
    @NotNull
    @Column(name = "organization_fiscal_code")
    private String organizationFiscalCode;

    // Debtor properties
    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;
    @NotNull
    @Column(name = "fiscal_code")
    private String fiscalCode;
    @NotNull
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "civic_number")
    private String civicNumber;
    @Column(name = "postal_code")
    private String postalCode;
    private String city;
    private String province;
    private String region;
    private String country;
    private String email;
    private String phone;

    // Payment Position properties
    @NotNull
    @Column(name = "company_name")
    private String companyName; // es. Comune di Roma
    @Column(name = "office_name")
    private String officeName; // es. Ufficio Tributi
    @NotNull
    @Column(name = "inserted_date")
    private LocalDateTime insertedDate;
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
    @Column(name = "validity_date")
    private LocalDateTime validityDate;
    @NotNull
    @Column(name = "min_due_date")
    private LocalDateTime minDueDate;
    @NotNull
    @Column(name = "max_due_date")
    private LocalDateTime maxDueDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DebtPositionStatus status;
    @NotNull
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Builder.Default
    @Column(name = "switch_to_expired", columnDefinition = "boolean DEFAULT false")
    private Boolean switchToExpired = false;

    @Builder.Default
    @Column(name = "pull", columnDefinition = "boolean DEFAULT true")
    private Boolean pull = true;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    @Builder.Default
    @NotNull
    @Version
    @Column(columnDefinition = "integer DEFAULT 0")
    private Integer version = 0;

    @Builder.Default
    @OneToMany(targetEntity = PaymentOption.class, fetch = FetchType.EAGER,
            mappedBy = "paymentPosition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentOption> paymentOption = new ArrayList<>();
}
