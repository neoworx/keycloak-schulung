package de.akdb.iam.keycloak.spi.scheduler;

import org.keycloak.provider.Provider;
import org.keycloak.timer.ScheduledTask;

public interface SchedulerTaskProvider extends Provider {
    ScheduledTask getScheduledTask();
}
