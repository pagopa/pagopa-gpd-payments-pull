package it.gov.pagopa.gpd.payments.pull.resources;

import it.gov.pagopa.gpd.payments.pull.config.Logged;
import it.gov.pagopa.gpd.payments.pull.exception.AppErrorException;
import it.gov.pagopa.gpd.payments.pull.exception.InvalidTaxCodeHeaderException;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static it.gov.pagopa.gpd.payments.pull.util.CommonUtil.mapToJSON;

/**
 * Payment Notices REST Resources
 */

@Tag(name = "Payment Notices", description = "Payment Notices Operations")
@Path("/payment-notices")
@Produces(value = MediaType.APPLICATION_JSON)
public class PaymentNotices {

    private static final String REGEX = "[\n\r]";
    private static final String REPLACEMENT = "_";
    private static final int FISCAL_CODE_LENGTH = 16;

    Logger logger = LoggerFactory.getLogger(PaymentNotices.class);

    @Inject
    PaymentNoticesService paymentNoticeService;

    /**
     * Recovers a reactive stream of payment notices, using the debtor taxCode, and optionally the dueDate for which at least one
     * Payment Option must be valid. Uses limit and page to limit result size
     *
     * @param taxCode debtor tax code to use for notices search. mandatory
     * @param dueDate optional parameter to filter notices based on valid dueDate
     * @param limit   page limit
     * @param page    page number
     * @return Response containing a reactive stream containing a list of notices, or a ErrorResponse in case of KO
     */
    @Operation(
            summary = "Get Payment Notices",
            description = "Retrieve payment notices from ACA and GPD sources, filtered by a due date")
    @SecurityRequirement(name = "ApiKey")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.ARRAY, implementation = PaymentNotice.class))
            ),
            @APIResponse(description = "Unexpected error occurred while recovering payment notices",
                    ref = "#/components/responses/InternalServerError"),
            @APIResponse(description = "Invalid or missing dueDate parameter",
                    ref = "#/components/responses/AppException400"),
    })
    @GET
    @Path("/v1")
    @Logged
    public Response getPaymentNotices(
            @Parameter(description = "Tax code to use for retrieving notices", required = true)
            @HeaderParam("x-tax-code") String taxCode,
            @Parameter(description = "Optional date to filter paymentNotices (if provided use the format yyyy-MM-dd)")
            @QueryParam("dueDate") LocalDate dueDate,
            @Valid @Positive @Max(100) @Parameter(description = "Number of elements on one page. Default = 50")
            @DefaultValue("50") @QueryParam("limit") @Schema(defaultValue = "50") Integer limit,
            @Valid @Min(0) @Parameter(description = "Page number. Page value starts from 0")
            @DefaultValue("0") @QueryParam("page") @Schema(defaultValue = "0") Integer page
    ) {
        var startTime = System.currentTimeMillis();
        Map<String, Object> args = new HashMap<>();
        args.put("taxCode", taxCode != null ? taxCode.hashCode() : "null");
        args.put("dueDate", dueDate);
        args.put("limit", limit);
        args.put("page", page);

        MDC.put("method", "getPaymentNotices");
        MDC.put("startTime", String.valueOf(startTime));
        MDC.put("args", mapToJSON(args));
        MDC.put("responseTime", String.valueOf(System.currentTimeMillis() - startTime));
        MDC.put("status", "OK");
        MDC.put("httpCode", "200");
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("operationId", UUID.randomUUID().toString());

        if (taxCode == null || taxCode.length() != FISCAL_CODE_LENGTH) {
            String errMsg = "Fiscal code " + taxCode + " header is null or not valid";
            throw new InvalidTaxCodeHeaderException(AppErrorCodeEnum.PPL_601, errMsg);
        }
        // replace new line and tab from user input to avoid log injection
        taxCode = taxCode.replaceAll(REGEX, REPLACEMENT);

        try {
            List<PaymentNotice> paymentNotices = this.paymentNoticeService.getPaymentNotices(taxCode, dueDate, limit, page);
            MDC.put("status", "OK");
            logger.info("Successfully API Invocation getPaymentNotices");
            return Response.ok().entity(paymentNotices).build();
        } catch (PaymentNoticeException e) {
            throw e;
        } catch (Exception e) {
            throw new AppErrorException(e);
        }
    }
}
