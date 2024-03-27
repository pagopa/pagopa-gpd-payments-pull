package it.gov.pagopa.gpd.payments.pull.service.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentOption;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.entity.Transfer;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.models.enums.DebtPositionStatus;
import it.gov.pagopa.gpd.payments.pull.models.enums.Type;
import it.gov.pagopa.gpd.payments.pull.repository.PaymentPositionRepository;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@QuarkusTest
class PaymentNoticesServiceImplTest {

    @InjectMock(convertScopes = true)
    PaymentPositionRepository paymentPositionRepository;

    @Inject
    private PaymentNoticesServiceImpl paymentNoticesService;

    @Test
    void getPaymentNoticesShouldReturnOK() {
        doReturn(Uni.createFrom().item(Collections.singletonList(createPaymentPosition())))
                .when(paymentPositionRepository).findPaymentPositionsByTaxCodeAndDueDate
                        (FISCAL_CODE, DUE_DATE, 50, 0);
        List<PaymentNotice> response = assertDoesNotThrow(() ->
                        paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0))
                .await().indefinitely();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("iupd", response.get(0).getIupd());
        assertEquals(1, response.get(0).getPaymentOptions().size());
        verify(paymentPositionRepository).findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, DUE_DATE, 50, 0);
    }

    @Test
    void getPaymentNoticesShouldReturnExceptionOnRepositoryError() {
        doReturn(Uni.createFrom().item(() -> {
            throw new RuntimeException();
        })).when(paymentPositionRepository).findPaymentPositionsByTaxCodeAndDueDate
                        (FISCAL_CODE, DUE_DATE, 50, 0);
        CompositeException paymentNoticeException =
                assertThrows(CompositeException.class, () ->
                paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0)
                .await().indefinitely());
        List<Throwable> causes = paymentNoticeException.getCauses();
        assertEquals(AppErrorCodeEnum.PPL_800, ((PaymentNoticeException) causes.get(causes.size()-1)).getErrorCode());
    }

    @Test
    void getPaymentNoticesShouldReturnExceptionOnMappingError() {
        PaymentPosition paymentPosition = createPaymentPosition();
        paymentPosition.setPaymentOption(null);
        doReturn(Uni.createFrom().item(Collections.singletonList(paymentPosition)))
                .when(paymentPositionRepository).findPaymentPositionsByTaxCodeAndDueDate
                        (FISCAL_CODE, DUE_DATE, 50, 0);
        CompositeException paymentNoticeException =
                assertThrows(CompositeException.class, () ->
                paymentNoticesService.getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0)
                        .await().indefinitely());
        List<Throwable> causes = paymentNoticeException.getCauses();
        assertEquals(AppErrorCodeEnum.PPL_800, ((PaymentNoticeException) causes.get(causes.size()-1)).getErrorCode());
    }

    PaymentPosition createPaymentPosition() {
        PaymentPosition paymentPosition = PaymentPosition.builder()
                .iupd("iupd")
                .status(DebtPositionStatus.VALID)
                .type(Type.F)
                .build();

        PaymentOption paymentOption = PaymentOption.builder()
                .amount(100)
                .isPartialPayment(false)
                .build();

        paymentOption.setTransfer(Collections.singletonList(
                Transfer.builder().amount(100).paymentOption(paymentOption).build())
        );
        paymentOption.setPaymentPosition(paymentPosition);
        paymentPosition.setPaymentOption(Collections.singletonList(paymentOption));

        return paymentPosition;
    }

}