package de.akdb.iam.keycloak.extension.userlastloginlistener;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@AutoService(EventListenerProviderFactory.class)
public class UserLastLoginListenerProviderFactory implements EventListenerProviderFactory {

    public static final String ID = "akdb-user-last-login-listener";

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new UserLastLoginListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return ID;
    }
}
