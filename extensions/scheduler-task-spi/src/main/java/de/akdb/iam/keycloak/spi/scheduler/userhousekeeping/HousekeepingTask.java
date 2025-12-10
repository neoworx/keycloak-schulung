package de.akdb.iam.keycloak.spi.scheduler.userhousekeeping;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.cluster.ClusterProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.timer.ScheduledTask;

import java.util.HashMap;
import java.util.Objects;

@JBossLog
public class HousekeepingTask implements ScheduledTask {

    public static final int _30_SECONDS = 30 * 1000;

    @Override
    public void run(KeycloakSession keycloakSession) {
        ClusterProvider clusterProvider = keycloakSession.getProvider(ClusterProvider.class);
        clusterProvider.executeIfNotExecuted("user-housekeeping", 5, () -> {
            RealmModel realm = keycloakSession.realms().getRealmByName("akdb");
            keycloakSession.getContext().setRealm(realm);
            keycloakSession.users().searchForUserStream(realm, new HashMap<>()).forEach(
                    userModel -> {
                        String lastLogin = userModel.getFirstAttribute("lastLogin");
                        if (Objects.nonNull(lastLogin)) {
                            long lastLoginTime = Long.parseLong(lastLogin);
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastLoginTime > _30_SECONDS) {
                                log.infof("Removing user %s due to inactivity.", userModel.getUsername());
                                keycloakSession.users().removeUser(realm, userModel);
                            }
                        }
                    }
            );

            return null;
        });
    }
}
