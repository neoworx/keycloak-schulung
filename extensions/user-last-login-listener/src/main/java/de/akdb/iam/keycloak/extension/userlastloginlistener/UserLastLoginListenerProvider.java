package de.akdb.iam.keycloak.extension.userlastloginlistener;

import lombok.RequiredArgsConstructor;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

@RequiredArgsConstructor
public class UserLastLoginListenerProvider implements EventListenerProvider {

    private final KeycloakSession keycloakSession;

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
