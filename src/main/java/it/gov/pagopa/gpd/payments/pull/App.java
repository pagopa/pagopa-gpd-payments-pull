package it.gov.pagopa.gpd.payments.pull;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import it.gov.pagopa.gpd.payments.pull.models.ErrorResponse;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.ServerVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;


@OpenAPIDefinition(
        info = @Info(title = "GPD Payments Pull Services", version = "${quarkus.application.version}"),
        servers = {
                @Server(url = "http://localhost:8080", description = "Localhost base URL"),
                @Server(url = "https://{host}/pagopa-gpd-payments-pull/v1", description = "Base URL",
                        variables = {
                                @ServerVariable(name = "host",
                                        enumeration = {"api.dev.platform.pagopa.it", "api.uat.platform.pagopa.it", "api.platform.pagopa.it"},
                                        defaultValue = "api.dev.platform.pagopa.it")})
        },
        components =
        @Components(
                securitySchemes = {
                        @SecurityScheme(
                                securitySchemeName = "ApiKey",
                                apiKeyName = "Ocp-Apim-Subscription-Key",
                                type = SecuritySchemeType.APIKEY,
                                in = SecuritySchemeIn.HEADER
                        )
                },
                responses = {
                        @APIResponse(
                                name = "InternalServerError",
                                responseCode = "500",
                                description = "Internal Server Error",
                                content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ErrorResponse.class),
                                        example =
                                                """
                                                        {
                                                           "type": "",
                                                           "title": "Internal Server Error",
                                                           "status": 500,
                                                           "detail": "An unexpected error has occurred. Please contact support.",
                                                           "instance": "PPL_603"
                                                         }""")),
                        @APIResponse(
                                name = "AppException400",
                                responseCode = "400",
                                description = "Default app exception for status 400",
                                content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ErrorResponse.class),
                                        examples =
                                        @ExampleObject(
                                                name = "Error",
                                                value =
                                                        """
                                                                {
                                                                   "type": "",
                                                                   "title": "Bad Request",
                                                                   "status": 400,
                                                                   "detail": "The provided due date [<due_date>] is invalid",
                                                                   "instance": "PPL_703"
                                                                 }"""))),
                        @APIResponse(
                                name = "AppException404",
                                responseCode = "404",
                                description = "Default app exception for status 404",
                                content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON,
                                        schema = @Schema(implementation = ErrorResponse.class),
                                        example =
                                                """
                                                        {
                                                           "type": "",
                                                           "title": "Not Found",
                                                           "status": 404,
                                                           "detail": "Payment Notice [<pn_id>] not found",
                                                           "instance": "PPL_900"
                                                         }""")),
                }))
@Startup
@QuarkusMain
public class App extends Application {

    public static void main(String[] args) {
        Quarkus.run(QuarkusApp.class, args);
    }

    public static class QuarkusApp implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Logger logger = LoggerFactory.getLogger(QuarkusApp.class);
            logger.info("QuarkusApp Run");
            Quarkus.waitForExit();
            return 0;
        }
    }

}
