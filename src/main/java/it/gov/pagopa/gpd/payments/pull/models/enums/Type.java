package it.gov.pagopa.gpd.payments.pull.models.enums;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public enum Type {
    @Schema(description = "Physical Debtor Type") F,
    @Schema(description = "Legal Debtor Type") G

}