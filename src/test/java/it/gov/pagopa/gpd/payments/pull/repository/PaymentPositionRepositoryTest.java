package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@QuarkusTest
class PaymentPositionRepositoryTest {

    @Inject
    PaymentPositionRepository paymentPositionRepository;

    @Test
    public void findNoticesOnRepositoryWithoutDueDate() {
        assertDoesNotThrow(() -> {
           paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                   FISCAL_CODE, null, 50, 0);
        });
    }

    @Test
    public void findNoticesOnRepositoryWithDueDate() {
        assertDoesNotThrow(() -> {
            paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                    FISCAL_CODE, DUE_DATE, 50, 0);
        });
    }

}