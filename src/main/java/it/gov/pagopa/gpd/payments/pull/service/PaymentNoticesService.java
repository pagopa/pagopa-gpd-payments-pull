package it.gov.pagopa.gpd.payments.pull.service;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;

import java.time.LocalDate;
import java.util.List;

public interface PaymentNoticesService {

    Uni<List<PaymentNotice>> getPaymentNotices(LocalDate dueDate);
}
