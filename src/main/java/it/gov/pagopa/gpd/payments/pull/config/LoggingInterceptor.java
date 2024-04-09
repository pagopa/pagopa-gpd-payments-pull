package it.gov.pagopa.gpd.payments.pull.config;

import io.quarkus.arc.Priority;
import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.quarkiverse.loggingjson.providers.KeyValueStructuredArgument.kv;
import static it.gov.pagopa.gpd.payments.pull.util.CommonUtil.mapToJSON;

@Logged
@Priority(2020)
@Interceptor
public class LoggingInterceptor {

    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static String getParams(InvocationContext context) {
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < context.getParameters().length; i++) {
            params.put(context.getMethod().getParameters()[i].getName(), context.getParameters()[i]);
        }
        return mapToJSON(params);

    }

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        var startTime = System.currentTimeMillis();

        String args = getParams(context);

        Object ret = null;
        try {
            ret = context.proceed();
        } catch (Exception e) {
            var endTime = System.currentTimeMillis();
            int httpCode = 400;
            String faultCode = "UNKNOWN";
            if(e instanceof PaymentNoticeException ex) {
                faultCode = ex.getErrorCode().getErrorCode();
                httpCode = ex.getErrorCode().getStatus().getStatusCode();
            }

            logger.error("Failed API Invocation getPaymentNotices",
                    kv("method", "getPaymentNotices"),
                    kv("startTime", startTime),
                    kv("args", mapToJSON(args)),
                    kv("responseTime", endTime - startTime),
                    kv("status", "KO"),
                    kv("httpCode", httpCode),
                    kv("requestId", UUID.randomUUID().toString()),
                    kv("operationId", UUID.randomUUID().toString()),
                    kv("faultCode", faultCode),
                    kv("faultDetail", e.getMessage()),
                    kv("response", mapToJSON(buildErrorResponse(httpCode, faultCode, e.getMessage()))));
            throw e;
        }

        return ret;
    }


    private ErrorResponse buildErrorResponse(int status, String errorCode, String message) {
        return ErrorResponse.builder()
                .title(String.valueOf(status))
                .status(status)
                .detail(message)
                .instance(errorCode)
                .build();
    }

}
