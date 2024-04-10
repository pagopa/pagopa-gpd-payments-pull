package it.gov.pagopa.gpd.payments.pull.mapper;

import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.entity.Transfer;
import it.gov.pagopa.gpd.payments.pull.models.Installment;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.PaymentOption;
import it.gov.pagopa.gpd.payments.pull.models.enums.PaymentNoticeStatus;

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
        return PaymentNotice
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
                .paymentOptions(paymentPosition.getPaymentOption().stream()
                        .map(item -> {
                            PaymentOption paymentOption = mapOption(item);
                            paymentOption.setSwitchToExpired(paymentOption.getSwitchToExpired());
                            return paymentOption;
                        }).toList())
                .build();
    }

    /**
     * Maps a PaymentOption model, starting from PaymentOption entity and related entities
     *
     * @param paymentOption instance of PaymentOption entity to use as mapping source
     * @return Mapped PaymentOption model instance
     */
    public static PaymentOption mapOption(it.gov.pagopa.gpd.payments.pull.entity.PaymentOption paymentOption) {
        return PaymentOption
                .builder()
                .description(paymentOption.getDescription())
                .amount(paymentOption.getAmount())
                .dueDate(paymentOption.getDueDate())
                .numberOfInstallments(paymentOption.getTransfer().size())
                .isPartialPayment(paymentOption.getIsPartialPayment())
                .switchToExpired(paymentOption.getPaymentPosition().getSwitchToExpired())
                .installments(paymentOption.getTransfer().stream().map(item ->
                        mapInstallment(item)).toList())
                .build();
    }

    /**
     * Maps an Installment instance using Transfer data, and related entities
     *
     * @param transfer instance of Transfer entity
     * @return mapped Installment instance
     */
    public static Installment mapInstallment(Transfer transfer) {
        return Installment.builder()
                .nav(transfer.getPaymentOption().getNav())
                .iuv(transfer.getIuv())
                .paTaxCode(transfer.getOrganizationFiscalCode())
                .paFullName(transfer.getPaymentOption().getPaymentPosition().getCompanyName()) //TODO: Missing name from option/transfer list
                .amount(transfer.getAmount())
                .description(transfer.getPaymentOption().getDescription()) //TODO: To define if remittance information to use
                .dueDate(transfer.getPaymentOption().getDueDate())
                .retentionDate(transfer.getPaymentOption().getRetentionDate())
                .insertedDate(transfer.getInsertedDate())
                .notificationFee(transfer.getPaymentOption().getNotificationFee())
                .status(transfer.getStatus())
                .lastUpdatedDate(transfer.getLastUpdatedDate())
                .build();
    }

}
