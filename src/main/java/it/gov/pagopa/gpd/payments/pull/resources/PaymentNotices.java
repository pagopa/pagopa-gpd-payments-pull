package it.gov.pagopa.gpd.payments.pull.resources;

import io.smallrye.mutiny.CompositeException;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

import static org.jboss.resteasy.reactive.RestResponse.StatusCode.INTERNAL_SERVER_ERROR;

@Tag(name = "Payment Notices", description = "Payment Notices Operations")
@Path("/payment-notices")
@Produces(value = MediaType.APPLICATION_JSON)
public class PaymentNotices {

    private final Logger logger = LoggerFactory.getLogger(PaymentNotices.class);

    @Inject
    private PaymentNoticesService paymentNoticeService;

    @Operation(
            summary = "Get Payment Notices",
            description = "Retrieve payment notices from ACA and GPD sources, filtered by a due date")
    @SecurityRequirement(name="ApiKey")
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
            @QueryParam("dueDate") LocalDate dueDate
    ) {
        Uni<List<PaymentNotice>> paymentNoticesUni = paymentNoticeService.getPaymentNotices(dueDate);
        return paymentNoticesUni.onFailure().invoke(error -> {
                    //TODO: Manage Error Checks
                    throw new RuntimeException(error);
                })
                .onItem().transform(item -> Response.ok().entity(item).build());
    }

    @ServerExceptionMapper
    public Response mapException(Exception exception) {

        //TODO: Define Exception Cases

        logger.error(exception.getMessage(), exception);

        if (exception instanceof CompositeException compositeException) {
            List<Throwable> causes = compositeException.getCauses();
            exception = (Exception) causes.get(causes.size()-1);
        }

        return Response.status(INTERNAL_SERVER_ERROR).build();

    }


}
