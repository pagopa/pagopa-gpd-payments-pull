package it.gov.pagopa.gpd.payments.pull.service;

import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;

import java.time.LocalDate;
import java.util.List;

/**
 * Services related to Payment Notices
 */
public interface PaymentNoticesService {

    /**
     * Recovers a reactive stream of payment notices, using the debtor taxCode, and optionally the dueDate for which at least one
     * Payment Option must be valid. Uses limit and page to limit result size
     *
     * @param taxCode debtor tax code to use for notices search. mandatory
     * @param dueDate optional parameter to filter notices based on valid dueDate
     * @param limit   page limit
     * @param page    page number
     * @return Uni stream containing a list of notices
     */
    List<PaymentNotice> getPaymentNotices(String taxCode, LocalDate dueDate, Integer limit, Integer page);

}
