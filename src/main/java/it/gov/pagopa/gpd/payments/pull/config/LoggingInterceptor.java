package it.gov.pagopa.gpd.payments.pull.config;

import it.gov.pagopa.gpd.payments.pull.exception.PaymentNoticeException;
import it.gov.pagopa.gpd.payments.pull.models.ErrorResponse;
import jakarta.annotation.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.Map;

import static it.gov.pagopa.gpd.payments.pull.util.CommonUtil.mapToJSON;

@Logged
@Priority(2020)
@Interceptor
public class LoggingInterceptor {

    Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static String getParams(InvocationContext context) {
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < context.getParameters().length; i++) {
            String name = context.getMethod().getParameters()[i].getName();
            Object value = context.getParameters()[i];
            if ("taxCode".equals(name) && value != null) {
                value = value.hashCode();
            }
            params.put(name, value);
        }
        return mapToJSON(params);
    }

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        var startTime = System.currentTimeMillis();
        String args = getParams(context);

        Object ret;
        try {
            ret = context.proceed();
        } catch (Exception e) {
            var endTime = System.currentTimeMillis();
            int httpCode = 400;
            String faultCode = "UNKNOWN";
            if (e instanceof PaymentNoticeException ex) {
                faultCode = ex.getErrorCode().getErrorCode();
                httpCode = ex.getErrorCode().getStatus().getStatusCode();
            }

            MDC.put("responseTime", String.valueOf(endTime - startTime));
            MDC.put("status", "KO");
            MDC.put("httpCode", String.valueOf(httpCode));
            MDC.put("faultCode", faultCode);
            MDC.put("faultDetail", e.getMessage());
            MDC.put("args", mapToJSON(args));
            MDC.put("response", mapToJSON(buildErrorResponse(httpCode, faultCode, e.getMessage())));
            logger.error("Failed API Invocation getPaymentNotices");
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
