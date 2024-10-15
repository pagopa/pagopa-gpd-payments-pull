package it.gov.pagopa.gpd.payments.pull.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(
        name = "payment_option_metadata",
        schema = "apd")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@paymentOptionMetadataId")
public class PaymentOptionMetadata extends PanacheEntityBase implements Serializable {


    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = -9014105148787448923L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_OPT_METADATA_SEQ")
    @SequenceGenerator(name = "PAYMENT_OPT_METADATA_SEQ", sequenceName = "PAYMENT_OPT_METADATA_SEQ", allocationSize = 1)
    private Long id;

    @NotNull
    private String key;

    private String value;

    @ManyToOne(
            targetEntity = PaymentOption.class,
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "payment_option_id")
    private PaymentOption paymentOption;

}
