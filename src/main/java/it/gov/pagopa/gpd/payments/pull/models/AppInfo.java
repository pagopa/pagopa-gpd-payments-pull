package it.gov.pagopa.gpd.payments.pull.models;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {
    private String name;
    private String version;
    private String environment;
}
