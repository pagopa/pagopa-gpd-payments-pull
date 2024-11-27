package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PaymentPositionRepository implements PanacheRepository<PaymentPosition> {

    @ConfigProperty(name = "app.payment_pull.keep_aca", defaultValue = "true")
    public boolean keepAca;

    // the pull flag has no business value -> for future needs
    private static final String BASE_QUERY = 
            "from PaymentPosition AS ppos Where ppos.fiscalCode = ?1 " +
            "AND ppos.status IN ('VALID', 'PARTIALLY_PAID') AND ppos.pull = true";

    private static final String DUE_DATE_QUERY =
            BASE_QUERY + " AND EXISTS (from ppos.paymentOption AS po WHERE po.dueDate >= ?2)";

    public String buildQuery(String query) {
        return keepAca
                ? query + " AND ppos.serviceType IN ('ACA', 'GPD')"
                : query + " AND ppos.serviceType = 'GPD'";
    }
    
    /**
     * Recovers a reactive stream of payment positions, using the debtor taxCode, and optionally the dueDate for which at least one
     * Payment Option must be valid. Uses limit and page to limit result size
     *
     * @param taxCode debtor tax code to use for notices search. mandatory
     * @param dueDate optional parameter to filter notices based on valid dueDate
     * @param limit   page limit
     * @param page    page number
     * @return
     */
    public List<PaymentPosition> findPaymentPositionsByTaxCodeAndDueDate(
            String taxCode, LocalDate dueDate, Integer limit, Integer page) {
    	
        String query = (dueDate == null) ? buildQuery(BASE_QUERY) : buildQuery(DUE_DATE_QUERY);

        return dueDate == null
                ? find(query, taxCode)
                      .page(Page.of(page, limit))
                      .list()
                : find(query, taxCode, dueDate.atStartOfDay())
                      .page(Page.of(page, limit))
                      .list();
    }
}
