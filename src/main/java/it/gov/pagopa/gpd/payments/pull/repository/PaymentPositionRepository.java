package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PaymentPositionRepository implements PanacheRepository<PaymentPosition> {

    private static final String GET_VALID_POSITIONS_BY_TAXCODE_BASE =
            "from PaymentPosition AS ppos Where ppos.fiscalCode = ?1 " +
                    "AND ppos.status IN ('VALID', 'PARTIALLY_PAID') AND ppos.pull = true ";

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
    public Uni<List<PaymentPosition>> findPaymentPositionsByTaxCodeAndDueDate(
            String taxCode, LocalDate dueDate, Integer limit, Integer page) {
        return (dueDate == null ?
                find(GET_VALID_POSITIONS_BY_TAXCODE_BASE, taxCode) :
                find(GET_VALID_POSITIONS_BY_TAXCODE_BASE.concat(" AND " +
                                "EXISTS (from ppos.paymentOption AS po WHERE po.dueDate >= ?2)"),
                        taxCode, dueDate.atStartOfDay()))
                .page(page, limit).list();
    }

}
