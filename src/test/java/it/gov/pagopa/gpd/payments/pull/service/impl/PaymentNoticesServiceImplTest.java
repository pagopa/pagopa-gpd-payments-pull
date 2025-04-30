package it.gov.pagopa.gpd.payments.pull.service.impl;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentOption;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.models.enums.DebtPositionStatus;
import it.gov.pagopa.gpd.payments.pull.models.enums.ServiceType;
import it.gov.pagopa.gpd.payments.pull.models.enums.Type;
import it.gov.pagopa.gpd.payments.pull.repository.PaymentPositionRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class PaymentNoticesServiceImplTest {

    @InjectMock
    PaymentPositionRepository paymentPositionRepository;

    @Inject
    private PaymentNoticesServiceImpl paymentNoticesService;

    @Test
    void getPaymentNoticesShouldReturnOK() {
        when(paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(FISCAL_CODE, DUE_DATE, 50, 0))
                .thenReturn(Arrays.asList(createPaymentPosition("", false, ServiceType.GPD),
                        createPaymentPosition("ACA_", false, ServiceType.ACA),
                        createPaymentPosition("PARTIAL_", true, ServiceType.GPD)));

        List<PaymentNotice> response = assertDoesNotThrow(() ->
                paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0));

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals("iupd", response.get(0).getIupd());
        assertEquals(1, response.get(0).getPaymentOptions().size());
        assertEquals(1, response.get(1).getPaymentOptions().size());
        assertEquals(2, response.get(2).getPaymentOptions().get(0).getInstallments().size());
        verify(paymentPositionRepository).findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, DUE_DATE, 50, 0);
    }

    @Test
    void getPaymentNoticesShouldReturnExceptionOnRepositoryError() {
        when(paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(FISCAL_CODE, DUE_DATE, 50, 0))
                .thenThrow(RuntimeException.class);

        PaymentNoticeException e = assertThrows(PaymentNoticeException.class, () ->
                        paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0));

        assertEquals(AppErrorCodeEnum.PPL_700, e.getErrorCode());
    }

    @Test
    void getPaymentNoticesShouldReturnExceptionOnMappingError() {
        PaymentPosition paymentPosition = createPaymentPosition("", true, ServiceType.GPD);
        paymentPosition.setPaymentOption(null);
        when(paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(FISCAL_CODE, DUE_DATE, 50, 0))
                .thenReturn(Collections.singletonList(paymentPosition));

        PaymentNoticeException e = assertThrows(PaymentNoticeException.class, () ->
                        paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0));

        assertEquals(AppErrorCodeEnum.PPL_800, e.getErrorCode());
    }

    private PaymentPosition createPaymentPosition(String prefix, Boolean isPartialPayment, ServiceType serviceType) {
        PaymentPosition paymentPosition = PaymentPosition.builder()
                .iupd(prefix + "iupd")
                .status(DebtPositionStatus.VALID)
                .type(Type.F)
                .serviceType(serviceType)
                .build();

        List<PaymentOption> paymentOption = new ArrayList<>(
                List.of(new PaymentOption[]{PaymentOption.builder()
                        .amount(100)
                        .dueDate(LocalDateTime.now())
                        .isPartialPayment(isPartialPayment)
                        .build()}));

        if (isPartialPayment) {
            paymentOption.add(PaymentOption.builder()
                    .amount(100)
                    .dueDate(LocalDateTime.now())
                    .isPartialPayment(true)
                    .build());
        }

        paymentPosition.setPaymentOption(paymentOption);

        return paymentPosition;
    }
}