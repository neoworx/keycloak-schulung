package de.akdb.iam.keycloak.spi.scheduler.userhousekeeping;

import org.keycloak.cluster.ClusterProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.timer.ScheduledTask;

public class HousekeepingTask implements ScheduledTask {
    @Override
    public void run(KeycloakSession keycloakSession) {
        ClusterProvider clusterProvider = keycloakSession.getProvider(ClusterProvider.class);
        clusterProvider.executeIfNotExecuted("user-housekeeping", 5, () -> {
            // magic goes here
            System.out.println("Running user housekeeping task...");
            return null;
        });
    }
}
