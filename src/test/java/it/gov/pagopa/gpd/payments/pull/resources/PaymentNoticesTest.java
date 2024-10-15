package it.gov.pagopa.gpd.payments.pull.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.gpd.payments.pull.models.ErrorResponse;
import it.gov.pagopa.gpd.payments.pull.models.PaymentNotice;
import it.gov.pagopa.gpd.payments.pull.models.enums.AppErrorCodeEnum;
import it.gov.pagopa.gpd.payments.pull.service.PaymentNoticesService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static it.gov.pagopa.gpd.payments.pull.Constants.DUE_DATE;
import static it.gov.pagopa.gpd.payments.pull.Constants.FISCAL_CODE;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@QuarkusTest
class PaymentNoticesTest {

    private static final String INVALID_DUE_DATE = "2024-04-21A";
    private static final String INVALID_FISCAL_CODE = "tooShort";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMock
    private PaymentNoticesService paymentNoticesService;

    @Test
    void getPaymentNoticesOnValidTaxCodeShouldReturnData() throws JsonProcessingException {
        doReturn(Uni.createFrom().item(Collections.singletonList(PaymentNotice.builder().build())))
                .when(paymentNoticesService).getPaymentNotices(FISCAL_CODE, null, 50, 0);
        String responseString =
                given()
                        .header("x-tax-code", FISCAL_CODE)
                        .when().get("/payment-notices/v1")
                        .then()
                        .statusCode(200)
                        .contentType("application/json")
                        .extract()
                        .asString();


        assertNotNull(responseString);
        List<PaymentNotice> response = objectMapper.readValue(responseString, List.class);
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(paymentNoticesService).getPaymentNotices(FISCAL_CODE, null, 50, 0);

    }

    @Test
    void getPaymentNoticesOnValidTaxCodeAndDateShouldReturnData() throws JsonProcessingException {
        doReturn(Uni.createFrom().item(Collections.singletonList(PaymentNotice.builder().build())))
                .when(paymentNoticesService).getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0);
        String responseString =
                given()
                        .header("x-tax-code", FISCAL_CODE)
                        .queryParam("dueDate", "2024-04-21")
                        .when().get("/payment-notices/v1")
                        .then()
                        .statusCode(200)
                        .contentType("application/json")
                        .extract()
                        .asString();


        assertNotNull(responseString);
        List<PaymentNotice> response = objectMapper.readValue(responseString, List.class);
        assertNotNull(response);
        assertEquals(1, response.size());
        verify(paymentNoticesService).getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0);
    }

//    @Test
//    void getPaymentNoticesOnInValidTaxCodeShouldReturnBadRequest() throws JsonProcessingException {
//        String responseString =
//                given()
//                        .header("x-tax-code", INVALID_FISCAL_CODE)
//                        .when().get("/payment-notices/v1")
//                        .then()
//                        .statusCode(400)
//                        .contentType("application/json")
//                        .extract()
//                        .asString();
//
//
//        assertNotNull(responseString);
//        ErrorResponse response = objectMapper.readValue(responseString, ErrorResponse.class);
//        assertNotNull(response);
//        assertEquals(AppErrorCodeEnum.PPL_601.getErrorCode(), response.getInstance());
//        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
//        assertEquals(BAD_REQUEST.getReasonPhrase(), response.getTitle());
//        assertNotNull(response.getDetail());
//        assertNotNull(response.getTitle());
//    }

    @Test
    void getPaymentNoticesOnServiceErrorShouldReturnIntServerError() throws JsonProcessingException {
        doReturn(Uni.createFrom().item(() -> {
            throw new RuntimeException();
        })).when(paymentNoticesService).getPaymentNotices(FISCAL_CODE, DUE_DATE, 50, 0);
        String responseString =
                given()
                        .header("x-tax-code", FISCAL_CODE)
                        .queryParam("dueDate", "2024-04-21")
                        .when().get("/payment-notices/v1")
                        .then()
                        .statusCode(500)
                        .contentType("application/json")
                        .extract()
                        .asString();

        assertNotNull(responseString);
        ErrorResponse response = objectMapper.readValue(responseString, ErrorResponse.class);
        assertNotNull(response);
        assertEquals(AppErrorCodeEnum.PPL_900.getErrorCode(), response.getInstance());
        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(INTERNAL_SERVER_ERROR.getReasonPhrase(), response.getTitle());
        assertNotNull(response.getDetail());
        assertNotNull(response.getTitle());
    }

    @Test
    void getPaymentNoticesOnMissingTaxCodeShouldReturnBadRequest() throws JsonProcessingException {
        String responseString =
                given()
                        .when().get("/payment-notices/v1")
                        .then()
                        .statusCode(400)
                        .contentType("application/json")
                        .extract()
                        .asString();


        assertNotNull(responseString);
        ErrorResponse response = objectMapper.readValue(responseString, ErrorResponse.class);
        assertNotNull(response);
        assertEquals(AppErrorCodeEnum.PPL_601.getErrorCode(), response.getInstance());
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals(BAD_REQUEST.getReasonPhrase(), response.getTitle());
        assertNotNull(response.getDetail());
        assertNotNull(response.getTitle());
    }


    @Test
    void getPaymentNoticesOnValidTaxCodeAndInvalidDateShouldBadRequest() throws JsonProcessingException {
        String responseString =
                given()
                        .header("x-tax-code", FISCAL_CODE)
                        .param("dueDate", INVALID_DUE_DATE)
                        .when().get("/payment-notices/v1")
                        .then()
                        .statusCode(400)
                        .contentType("application/json")
                        .extract()
                        .asString();


        assertNotNull(responseString);
        ErrorResponse response = objectMapper.readValue(responseString, ErrorResponse.class);
        assertNotNull(response);
        assertEquals(AppErrorCodeEnum.PPL_600.getErrorCode(), response.getInstance());
        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals(BAD_REQUEST.getReasonPhrase(), response.getTitle());
        assertNotNull(response.getDetail());
        assertNotNull(response.getTitle());
    }

}
