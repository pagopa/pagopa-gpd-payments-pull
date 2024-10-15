package it.gov.pagopa.gpd.payments.pull.service.impl;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.mapper.PaymentNoticeMapper;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.repository.PaymentPositionRepository;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PaymentNoticesServiceImpl implements PaymentNoticesService {

    @Inject
    PaymentPositionRepository paymentPositionRepository;

    @ConfigProperty(name = "app.payment_pull.keep_aca", defaultValue = "true")
    Boolean keepAca;

    @Override
    public Uni<List<PaymentNotice>> getPaymentNotices(String taxCode, LocalDate dueDate, Integer limit, Integer page) {
        return this.paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(taxCode, dueDate, limit, page)
                .onFailure().invoke(Unchecked.consumer(throwable -> {
                    throw buildPaymentNoticeException(AppErrorCodeEnum.PPL_700, throwable);
                }))
                .onItem().transform(paymentPositions -> paymentPositions.stream()
                        .filter(item -> keepAca || !item.getIupd().contains("ACA"))
                        .map(PaymentNoticeMapper::manNotice)
                        .toList())
                .onFailure().invoke(Unchecked.consumer(throwable -> {
                    throw buildPaymentNoticeException(AppErrorCodeEnum.PPL_800, throwable);
                }));
    }

    private PaymentNoticeException buildPaymentNoticeException(AppErrorCodeEnum errorCodeEnum, Throwable throwable) {
        return new PaymentNoticeException(errorCodeEnum, String.format("Exception thrown during data recovery: %s", throwable), throwable);
    }
}
