package it.gov.pagopa.gpd.payments.pull.service.impl;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PaymentNoticesServiceImpl implements PaymentNoticesService {


    @Override
    public Uni<List<PaymentNotice>> getPaymentNotices(LocalDate dueDate) {
        return null;
    }

}
