package it.gov.pagopa.gpd.payments.pull.resources;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import it.gov.pagopa.gpd.payments.pull.exception.AppErrorException;
import it.gov.pagopa.gpd.payments.pull.exception.InvalidTaxCodeHeaderException;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

/**
 * Payment Notices REST Resources
 */

@Tag(name = "Payment Notices", description = "Payment Notices Operations")
@Path("/payment-notices/v1")
@Produces(value = MediaType.APPLICATION_JSON)
public class PaymentNotices {

    private static final String REGEX = "[\n\r]";
    private static final String REPLACEMENT = "_";


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
            @APIResponse(description = "Unexpected error occured while recovering payment notices",
                    ref = "#/components/responses/InternalServerError"),
            @APIResponse(description = "Invalid or missing dueDate parameter",
                    ref = "#/components/responses/AppException400"),
    })
    @GET
    public Uni<Response> getPaymentNotices(
            @Parameter(description = "Tax code to use for retrieving notices", required = true)
            @HeaderParam("x-tax-code") String taxCode,
            @Parameter(description = "Optional date to filter paymentNotices (if provided use the format yyyy-MM-dd)")
            @QueryParam("dueDate") LocalDate dueDate,
            @Valid @Positive @Max(100) @Parameter(description = "Number of elements on one page. Default = 50")
            @DefaultValue("50") @QueryParam("limit") Integer limit,
            @Valid @Min(0) @Parameter(description = "Page number. Page value starts from 0")
            @DefaultValue("0") @QueryParam("page") Integer page
    ) {

        if(taxCode == null) {
            String errMsg = "Fiscal code header is null or not valid";
            throw new InvalidTaxCodeHeaderException(AppErrorCodeEnum.PPL_601, errMsg);
        }
        // replace new line and tab from user input to avoid log injection
        taxCode = taxCode.replaceAll(REGEX, REPLACEMENT);

        Uni<List<PaymentNotice>> paymentNoticesUni = paymentNoticeService.getPaymentNotices(taxCode, dueDate, limit, page);
        return paymentNoticesUni.onFailure().invoke(Unchecked.consumer(error -> {
                    if(error instanceof PaymentNoticeException ex)
                        throw new PaymentNoticeException(ex.getErrorCode(), ex.getMessage(), ex.getCause());
                    else
                        throw new AppErrorException(error);
                }))
                .onItem()
                .transform(item -> Response.ok().entity(item).build());
    }


}
