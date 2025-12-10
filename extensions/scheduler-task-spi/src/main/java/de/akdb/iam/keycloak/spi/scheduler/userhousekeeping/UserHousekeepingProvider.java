package de.akdb.iam.keycloak.spi.scheduler.userhousekeeping;

import de.akdb.iam.keycloak.spi.scheduler.SchedulerTaskProvider;
import org.keycloak.timer.ScheduledTask;

public class UserHousekeepingProvider implements SchedulerTaskProvider {
    @Override
    public ScheduledTask getScheduledTask() {
        return new HousekeepingTask();
    }

    @Override
    public void close() {

    }
}
