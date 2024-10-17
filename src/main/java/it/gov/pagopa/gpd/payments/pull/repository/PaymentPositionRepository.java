package it.gov.pagopa.gpd.payments.pull.repository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.entity.PaymentPosition;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PaymentPositionRepository implements PanacheRepository<PaymentPosition> {

    private static final String GET_VALID_POSITIONS_BY_TAXCODE_BASE =
            "from PaymentPosition AS ppos Where ppos.fiscalCode = ?1 " +
                    "AND ppos.status IN ('VALID', 'PARTIALLY_PAID') AND ppos.pull = true";
    private static final String GET_VALID_POSITIONS_BY_TAXCODE_AND_DUE_DATE =
            "from PaymentPosition AS ppos Where ppos.fiscalCode = ?1 " +
                    "AND ppos.status IN ('VALID', 'PARTIALLY_PAID') AND ppos.pull = true " +
                    "AND EXISTS (from ppos.paymentOption AS po WHERE po.dueDate >= ?2)";

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
            String taxCode, LocalDate dueDate, Integer limit, Integer page
    ) {
        if (dueDate == null) {
            return Panache.getSession()
                            .onItem()
                            .transformToUni(session ->
                                    session.createNativeQuery(
                                            "SELECT id, city, civic_number, company_name, country, email, fiscal_code, full_name, inserted_date, iupd, last_updated_date, max_due_date, min_due_date, office_name, organization_fiscal_code, phone, postal_code, province, publish_date, region, status, street_name, type, validity_date, version, switch_to_expired, payment_date, pull, pay_stand_in " +
                                                    "FROM apd.payment_position ppos " +
                                                    "WHERE ppos.fiscal_code = ?1 " +
                                                    "AND ppos.status IN ('VALID', 'PARTIALLY_PAID') " +
                                                    "AND ppos.pull = true ", PaymentPosition.class)
                                            .setParameter(1, taxCode).getResultList());

        }

        return find(GET_VALID_POSITIONS_BY_TAXCODE_AND_DUE_DATE, taxCode, dueDate.atStartOfDay())
                .page(Page.of(page, limit))
                .list();
    }
}
