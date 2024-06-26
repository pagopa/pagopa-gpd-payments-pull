package it.gov.pagopa.gpd.payments.pull.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Model class for the error response
 */
@Getter
@Builder
@Jacksonized
@JsonPropertyOrder({"title", "status", "detail", "instance"})
@RegisterForReflection
public class ErrorResponse {

    @Schema(example = "Internal Server Error")
    private String title;

    @Schema(example = "500")
    private int status;

    @Schema(example = "An unexpected error has occurred. Please contact support.")
    private String detail;

    @Schema(example = "PN-500")
    private String instance;
}
