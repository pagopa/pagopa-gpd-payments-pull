package it.gov.pagopa.gpd.payments.pull.service.impl;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.mapper.PaymentNoticeMapper;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.repository.PaymentPositionRepository;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PaymentNoticesServiceImpl implements PaymentNoticesService {

    @Inject
    PaymentPositionRepository paymentPositionRepository;

    @Override
    public Uni<List<PaymentNotice>> getPaymentNotices(String taxCode, LocalDate dueDate, Integer limit, Integer page) {
        return paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(taxCode, dueDate, limit, page)
                .onFailure().invoke(throwable -> {
                    throw new PaymentNoticeException(AppErrorCodeEnum.PPL_700,
                            String.format("Exception thrown during data recovery: %s", throwable));
                })
                .onItem().transform(item -> item.stream().map(PaymentNoticeMapper::manNotice)
                        .toList()).onFailure().invoke(throwable -> {
                            throw new PaymentNoticeException(AppErrorCodeEnum.PPL_800,
                        String.format("Exception thrown during data recovery: %s", throwable));
        });
    }

}
