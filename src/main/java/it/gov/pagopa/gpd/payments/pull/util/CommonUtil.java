package it.gov.pagopa.gpd.payments.pull.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.gov.pagopa.gpd.payments.pull.exception.AppErrorException;

public class CommonUtil {

    public static String mapToJSON(Object result) {
        try {
            return new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new AppErrorException(e);
        }
    }
}
