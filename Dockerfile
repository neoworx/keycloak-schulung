FROM maven:3-eclipse-temurin-21-alpine AS build

RUN mkdir /build
WORKDIR /build
ADD . /build
RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM keycloak/keycloak:26.4.7
COPY --from=build /build/extensions/session-limiter-listener/target/*.jar /opt/keycloak/providers/
COPY --from=build /build/extensions/user-last-login-listener/target/*.jar /opt/keycloak/providers/