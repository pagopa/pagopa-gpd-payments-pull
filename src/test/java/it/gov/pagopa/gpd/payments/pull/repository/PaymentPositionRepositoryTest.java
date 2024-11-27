package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import it.gov.pagopa.gpd.payments.pull.models.enums.DebtPositionStatus;
import it.gov.pagopa.gpd.payments.pull.models.enums.ServiceType;
import it.gov.pagopa.gpd.payments.pull.models.enums.Type;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class PaymentPositionRepositoryTest {

    @InjectSpy
    PaymentPositionRepository paymentPositionRepository;
    

    @Test
    void findNoticesOnRepositoryWithoutDueDate() {
    	// force keepAca to true
    	paymentPositionRepository.keepAca = true;
        PanacheQuery panacheQuery = Mockito.mock(PanacheQuery.class);
        when(paymentPositionRepository.find(paymentPositionRepository.buildQueryBase(), FISCAL_CODE)).thenReturn(panacheQuery);
        when(panacheQuery.page(any())).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(Collections.singletonList(
                PaymentPosition.builder()
                        .iupd("iupd")
                        .status(DebtPositionStatus.VALID)
                        .type(Type.F)
                        .serviceType(ServiceType.GPD)
                        .build()));

        List<PaymentPosition> result = assertDoesNotThrow(() -> paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, null, 50, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    void findNoticesOnRepositoryWithDueDate() {
    	// force keepAca to true
    	paymentPositionRepository.keepAca = true;
        PanacheQuery panacheQuery = Mockito.mock(PanacheQuery.class);
        when(paymentPositionRepository.find(paymentPositionRepository.buildQueryWithDueDate(), FISCAL_CODE, DUE_DATE.atStartOfDay()))
                .thenReturn(panacheQuery);
        when(panacheQuery.page(any())).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(Collections.singletonList(
                PaymentPosition.builder()
                        .iupd("iupd")
                        .status(DebtPositionStatus.VALID)
                        .type(Type.F)
                        .serviceType(ServiceType.GPD)
                        .build()));

        List<PaymentPosition> result = assertDoesNotThrow(() -> paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, DUE_DATE, 50, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void findNoticesOnRepositoryForKeepACAFalse() {
    	// force keepAca to false
    	paymentPositionRepository.keepAca = false;
        PanacheQuery panacheQuery = Mockito.mock(PanacheQuery.class);
        when(paymentPositionRepository.find(paymentPositionRepository.buildQueryBase(), FISCAL_CODE)).thenReturn(panacheQuery);
        when(panacheQuery.page(any())).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(Collections.singletonList(
                PaymentPosition.builder()
                        .iupd("iupd")
                        .status(DebtPositionStatus.VALID)
                        .type(Type.F)
                        .serviceType(ServiceType.GPD)
                        .build()));

        List<PaymentPosition> result = assertDoesNotThrow(() -> paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, null, 50, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}