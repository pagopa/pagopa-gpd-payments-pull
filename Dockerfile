## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-graalvmce-builder-image:22.3-java17 AS build
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
ARG QUARKUS_PROFILE
ARG APP_NAME

USER root
RUN chmod 777 /code/agent/config.yaml
# install wget
RUN  microdnf  install -y wget
# install jmx agent
RUN cd /code && \
    wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.19.0/jmx_prometheus_javaagent-0.19.0.jar && \
    curl -o 'opentelemetry-javaagent.jar' -L 'https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.25.1/opentelemetry-javaagent.jar' && \
    curl -o 'applicationinsights-agent.jar' -L 'https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.4.17/applicationinsights-agent-3.4.17.jar'

# build the application
RUN ./mvnw package -DskipTests=true -Dquarkus.application.name=$APP_NAME -Dquarkus.profile=$QUARKUS_PROFILE

RUN mkdir -p /code/target/otel && \
    chmod 777 /code/opentelemetry-javaagent.jar && \
    cp /code/opentelemetry-javaagent.jar /code/target/otel/opentelemetry-javaagent.jar

RUN mkdir -p /code/target/appins && \
    chmod 777 /code/applicationinsights-agent.jar && \
    cp /code/applicationinsights-agent.jar /code/target/appins/applicationinsights-agent.jar

RUN mkdir -p /code/target/jmx && \
    cp /code/agent/config.yaml /code/target/jmx/config.yaml

RUN chmod 777 /code/jmx_prometheus_javaagent-0.19.0.jar && \
    cp /code/jmx_prometheus_javaagent-0.19.0.jar /code/target/jmx/jmx_prometheus_javaagent-0.19.0.jar

## Stage 2 : create the docker final image
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=build /code/target/*-runner /work/application
COPY --from=build /code/target/jmx/ /work/
COPY --from=build /code/target/otel/ /work/
COPY --from=build /code/target/appins/ /work/

# set up permissions for user `1001`
RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
