package it.gov.pagopa.gpd.payments.pull.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.gov.pagopa.gpd.payments.pull.models.enums.PaymentOptionStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(
        name = "payment_option",
        schema = "apd"
)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@paymentOptionId")
public class PaymentOption implements Serializable {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -2800191377721368418L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_OPT_SEQ")
    @SequenceGenerator(name = "PAYMENT_OPT_SEQ", sequenceName = "PAYMENT_OPT_SEQ", allocationSize = 1)
    private Long id;

    @NotNull
    private String nav;

    @NotNull
    private String iuv;

    @NotNull
    @Column(name = "organization_fiscal_code")
    private String organizationFiscalCode;

    @NotNull
    private long amount;
    private String description;

    @NotNull
    @Column(name = "is_partial_payment")
    private Boolean isPartialPayment;

    @NotNull
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "retention_date")
    private LocalDateTime retentionDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "reporting_date")
    private LocalDateTime reportingDate;

    @NotNull
    @Column(name = "inserted_date")
    private LocalDateTime insertedDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    private long fee;

    @Column(name = "notification_fee")
    private long notificationFee;

    @Column(name = "psp_company")
    private String pspCompany;

    @Column(name = "receipt_id")
    private String idReceipt;

    @Column(name = "flow_reporting_id")
    private String idFlowReporting;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentOptionStatus status;

    @NotNull
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "last_updated_date_notification_fee")
    private LocalDateTime lastUpdatedDateNotificationFee;

    // flag that identifies if the payment option has a payment in progress (false = no payment in progress)
    @Builder.Default
    @Transient
    private boolean paymentInProgress = false;

    @ManyToOne(
            targetEntity = PaymentPosition.class,
            fetch = FetchType.EAGER,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "payment_position_id", nullable = false)
    private PaymentPosition paymentPosition;

    @Builder.Default
    @OneToMany(
            targetEntity = Transfer.class,
            fetch = FetchType.LAZY,
            mappedBy = "paymentOption",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Transfer> transfer = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            targetEntity = PaymentOptionMetadata.class,
            fetch = FetchType.LAZY,
            mappedBy = "paymentOption",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PaymentOptionMetadata> paymentOptionMetadata = new ArrayList<>();

}
