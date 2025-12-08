package de.akdb.iam.keycloak.extension.sessionlimiterlistener;

import com.google.auto.service.AutoService;
import lombok.RequiredArgsConstructor;
import org.keycloak.Config;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;

import java.util.Comparator;
import java.util.List;

@AutoService(EventListenerProviderFactory.class)
public class SessionLimiterListenerProviderFactory implements EventListenerProviderFactory {

    public static final String ID = "akdb-session-limiter-listener";
    public static final int SESSION_COUNT_LIMIT = 2;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new SessionLimiterListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {
        // Implementation goes here
    }

    @Override
    public String getId() {
        return ID;
    }
}
