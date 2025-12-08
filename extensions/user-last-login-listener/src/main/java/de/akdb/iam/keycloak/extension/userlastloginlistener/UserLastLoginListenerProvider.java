package de.akdb.iam.keycloak.extension.userlastloginlistener;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;


public class UserLastLoginListenerProvider implements EventListenerProvider {

    private KeycloakSession keycloakSession;

    public UserLastLoginListenerProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(EventType.LOGIN)) {
            String userId = event.getUserId();
            RealmModel realm = keycloakSession.getContext().getRealm();
            UserModel user = keycloakSession.users().getUserById(realm, userId);
            if (user != null) {
                user.setSingleAttribute("lastLogin", event.getTime() + "");
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        // intentionally left blank
    }

    @Override
    public void close() {
        // intentionally left blank
    }
}
