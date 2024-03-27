package it.gov.pagopa.gpd.payments.pull.exception.mapper;

import io.smallrye.mutiny.CompositeException;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.ErrorResponse;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;

import static it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException.getHttpStatus;
import static org.jboss.resteasy.reactive.RestResponse.Status.BAD_REQUEST;
import static org.jboss.resteasy.reactive.RestResponse.StatusCode.INTERNAL_SERVER_ERROR;

public class ExceptionMapper {

    Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

    private ErrorResponse buildErrorResponse(Response.Status status, AppErrorCodeEnum errorCode, String message) {
        return ErrorResponse.builder()
                .title(status.getReasonPhrase())
                .status(status.getStatusCode())
                .detail(message)
                .instance(errorCode.getErrorCode())
                .build();
    }

    @ServerExceptionMapper
    public Response mapCompositeException(CompositeException exception) {
        logger.error(exception.getMessage(), exception);
        Exception composedException;
        List<Throwable> causes = exception.getCauses();
        composedException = (Exception) causes.get(causes.size()-1);

        if (composedException instanceof NotFoundException) {
            return mapNotFoundException((NotFoundException) composedException);
        }
        else if (composedException instanceof PaymentNoticeException paymentNoticeException) {
            return mapPaymentNoticeException(paymentNoticeException);
        } else {
            return mapGenericException(exception);
        }

    }

    @ServerExceptionMapper
    public Response mapNotFoundException(NotFoundException exception) {
        logger.error(exception.getMessage(), exception);
        return Response.status(BAD_REQUEST).entity(
                buildErrorResponse(Response.Status.BAD_REQUEST, AppErrorCodeEnum.PPL_600,
                        "Invalid parameters on request")).build();
    }

    @ServerExceptionMapper
    public Response mapPaymentNoticeException(PaymentNoticeException exception) {
        logger.error(exception.getMessage(), exception);
        Response.Status status = getHttpStatus(exception);
        return Response.status(status).entity(buildErrorResponse(status,
                exception.getErrorCode(), exception.getMessage())).build();
    }

    @ServerExceptionMapper
    public Response mapGenericException(Exception exception) {
        logger.error(exception.getMessage(), exception);
        return Response.status(INTERNAL_SERVER_ERROR)
                .entity(
                        buildErrorResponse(
                                Response.Status.INTERNAL_SERVER_ERROR,
                                AppErrorCodeEnum.PPL_900,
                                "Unexpected Error"))
                .build();
    }

}
