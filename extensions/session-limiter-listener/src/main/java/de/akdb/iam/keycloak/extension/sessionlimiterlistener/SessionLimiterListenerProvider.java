package de.akdb.iam.keycloak.extension.sessionlimiterlistener;

import com.google.auto.service.AutoService;
import lombok.NoArgsConstructor;
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
@RequiredArgsConstructor
public class SessionLimiterListenerProvider implements EventListenerProvider, EventListenerProviderFactory {

    public static final String ID = "akdb-session-limiter-listener";
    public static final int SESSION_COUNT_LIMIT = 2;

    private final KeycloakSession keycloakSession;

    public SessionLimiterListenerProvider() {
        this.keycloakSession = null;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().equals(EventType.LOGIN)) {
            String userId = event.getUserId();
            RealmModel realm = this.keycloakSession.getContext().getRealm();
            UserModel user = keycloakSession.users().getUserById(realm, userId);
            List<UserSessionModel> sortedSessions = keycloakSession.sessions().getUserSessionsStream(realm, user)
                    .sorted(Comparator.comparingLong(UserSessionModel::getStarted))
                    .toList();

            int sessionsToBeRemoved = sortedSessions.size() - SESSION_COUNT_LIMIT;
            if (sessionsToBeRemoved > 0) {
                sortedSessions.stream()
                        .limit(sortedSessions.size() - SESSION_COUNT_LIMIT)
                        .forEach(userSession -> this.keycloakSession.sessions().removeUserSession(realm, userSession));
                System.out.println("Removed " + sessionsToBeRemoved + " sessions for user: " + user.getUsername());
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        // Implementation goes here
    }

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
