package it.gov.pagopa.gpd.payments.pull.service.impl;

import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.mapper.PaymentNoticeMapper;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.repository.PaymentPositionRepository;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PaymentNoticesServiceImpl implements PaymentNoticesService {

    @Inject
    PaymentPositionRepository paymentPositionRepository;

    @Override
    public List<PaymentNotice> getPaymentNotices(String taxCode, LocalDate dueDate, Integer limit, Integer page) {
        try {
            return getPositions(taxCode, dueDate, limit, page).parallelStream()
                    .map(PaymentNoticeMapper::manNotice)
                    .toList();
        } catch (PaymentNoticeException e) {
            throw e;
        } catch (Exception e) {
            throw buildPaymentNoticeException(AppErrorCodeEnum.PPL_800, e);
        }
    }

    private List<PaymentPosition> getPositions(
            String taxCode,
            LocalDate dueDate,
            Integer limit,
            Integer page
    ) {
        try {
            return this.paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(taxCode, dueDate, limit, page);
        } catch (Exception e) {
            throw buildPaymentNoticeException(AppErrorCodeEnum.PPL_700, e);
        }
    }

    private PaymentNoticeException buildPaymentNoticeException(AppErrorCodeEnum errorCodeEnum, Throwable throwable) {
        return new PaymentNoticeException(errorCodeEnum, String.format("Exception thrown during data recovery: %s", throwable), throwable);
    }
}
