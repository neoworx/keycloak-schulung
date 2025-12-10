package de.akdb.iam.keycloak.spi.scheduler.userhousekeeping;

import com.google.auto.service.AutoService;
import de.akdb.iam.keycloak.spi.scheduler.SchedulerTaskProvider;
import de.akdb.iam.keycloak.spi.scheduler.SchedulerTaskProviderFactory;
import org.keycloak.models.KeycloakSession;

@AutoService(SchedulerTaskProviderFactory.class)
public class UserHousekeepingProviderFactory extends SchedulerTaskProviderFactory {
    @Override
    public SchedulerTaskProvider create(KeycloakSession keycloakSession) {
        return new UserHousekeepingProvider();
    }

    @Override
    public String getId() {
        return "akdb-user-housekeeping-provider";
    }
}
