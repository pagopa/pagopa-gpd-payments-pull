package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;

@QuarkusTest
class PaymentPositionRepositoryTest {

    @InjectMock
    PaymentPositionRepository paymentPositionRepository;

    @Test
    @RunOnVertxContext
    void findNoticesOnRepositoryWithoutDueDate(UniAsserter asserter) {
        asserter.execute(() -> Mockito.when(paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                        FISCAL_CODE, null, 50, 0))
                .thenReturn(Uni.createFrom().item(Collections.emptyList())));

        asserter.assertThat(() -> paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, null, 50, 0), List::isEmpty);
    }

    @Test
    @RunOnVertxContext
    void findNoticesOnRepositoryWithDueDate(UniAsserter asserter) {
        List<PaymentPosition> paymentPosition = Collections.singletonList(PaymentPosition.builder().iupd("test-with-data").build());

        asserter.execute(() -> Mockito.when(paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                        FISCAL_CODE, DUE_DATE, 50, 0))
                .thenReturn(Uni.createFrom().item(paymentPosition)));

        asserter.assertEquals(() -> paymentPositionRepository.findPaymentPositionsByTaxCodeAndDueDate(
                FISCAL_CODE, DUE_DATE, 50, 0), paymentPosition);
    }

}