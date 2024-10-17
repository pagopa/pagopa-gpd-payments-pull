package it.gov.pagopa.gpd.payments.pull.mapper;

import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.entity.Transfer;
import it.gov.pagopa.gpd.payments.pull.models.Installment;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.PaymentOption;
import it.gov.pagopa.gpd.payments.pull.models.enums.PaymentNoticeStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Payment Notice mapping methods
 */
public class PaymentNoticeMapper {

    /**
     * Maps a paymentNotice, starting from an instance of PaymentPosition, complete with internal lists using entity relations
     *
     * @param paymentPosition instance of PaymentPosition to be mapped
     * @return mapped PaymentNotice instance
     */
    public static PaymentNotice manNotice(PaymentPosition paymentPosition) {
        PaymentNotice paymentNotice = PaymentNotice
                .builder()
                .iupd(paymentPosition.getIupd())
                .debtorFullName(paymentPosition.getFullName())
                .debtorTaxCode(paymentPosition.getFiscalCode())
                .insertedDate(paymentPosition.getInsertedDate())
                .validityDate(paymentPosition.getValidityDate())
                .lastUpdateDate(paymentPosition.getLastUpdatedDate())
                .status(PaymentNoticeStatus.valueOf(paymentPosition.getStatus().toString()))
                .paTaxCode(paymentPosition.getOrganizationFiscalCode())
                .paFullName(paymentPosition.getCompanyName())
                .publishDate(paymentPosition.getPublishDate())
                .debtorType(paymentPosition.getType().toString())
                .build();
        List<PaymentOption> paymentOptions = new java.util.ArrayList<>(paymentPosition.getPaymentOption().stream()
                .filter(item -> !item.getIsPartialPayment()).map(
                        item -> mapOptions(paymentPosition, Collections.singletonList(item))).toList());
        if (paymentOptions.size() != paymentPosition.getPaymentOption().size()) {
            paymentOptions.add(mapOptions(paymentPosition, paymentPosition.getPaymentOption().stream()
                    .filter(it.gov.pagopa.gpd.payments.pull.entity.PaymentOption::getIsPartialPayment).toList()));
        }
        paymentNotice.setPaymentOptions(paymentOptions);
        return paymentNotice;
    }

    /**
     * Maps a PaymentOption model, starting from PaymentOption entity and related entities
     *
     * @param paymentPosition instance of PaymentOption entity to use as mapping source
     * @return Mapped PaymentOption model instance
     */
    public static PaymentOption mapOptions(
            PaymentPosition paymentPosition,
            List<it.gov.pagopa.gpd.payments.pull.entity.PaymentOption> paymentOptions) {
        return PaymentOption
                .builder()
                .description(paymentOptions.get(0).getDescription())
                .amount(paymentOptions.stream().map(
                        it.gov.pagopa.gpd.payments.pull.entity.PaymentOption::getAmount).reduce(0L, Long::sum))
                .dueDate(paymentOptions.stream().map(it.gov.pagopa.gpd.payments.pull.entity.PaymentOption::getDueDate)
                        .max(LocalDateTime::compareTo).get())
                .numberOfInstallments(paymentOptions.size())
                .isPartialPayment(paymentOptions.get(0).getIsPartialPayment())
                .switchToExpired(paymentPosition.getSwitchToExpired())
                .installments(paymentOptions.stream().map(item ->
                        mapInstallment(item, paymentPosition)).toList())
                .build();
    }

    /**
     * Maps an Installment instance using PaymentOption data, and related entities
     *
     * @param paymentOption instance of PaymentOption entity
     * @return mapped Installment instance
     */
    public static Installment mapInstallment(
            it.gov.pagopa.gpd.payments.pull.entity.PaymentOption paymentOption, PaymentPosition paymentPosition) {
        return Installment.builder()
                .nav(paymentOption.getNav())
                .iuv(paymentOption.getIuv())
                .paTaxCode(paymentOption.getOrganizationFiscalCode())
                .paFullName(paymentPosition.getCompanyName()) //TODO: Missing name from option/transfer list
                .amount(paymentOption.getAmount())
                .description(paymentOption.getDescription()) //TODO: To define if remittance information to use
                .dueDate(paymentOption.getDueDate())
                .retentionDate(paymentOption.getRetentionDate())
                .insertedDate(paymentOption.getInsertedDate())
                .notificationFee(paymentOption.getNotificationFee())
                .status(paymentOption.getStatus())
                .lastUpdatedDate(paymentOption.getLastUpdatedDate())
                .build();
    }

}
