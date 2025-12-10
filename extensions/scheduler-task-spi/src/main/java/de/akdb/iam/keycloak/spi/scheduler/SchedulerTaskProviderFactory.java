package de.akdb.iam.keycloak.spi.scheduler;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.utils.PostMigrationEvent;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.timer.ScheduledTask;
import org.keycloak.timer.TimerProvider;

public abstract class SchedulerTaskProviderFactory implements ProviderFactory<SchedulerTaskProvider> {

    private KeycloakSessionFactory keycloakSessionFactory;

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        this.keycloakSessionFactory = keycloakSessionFactory;
        keycloakSessionFactory.register((event) -> {
            if (event instanceof PostMigrationEvent) {
                try (KeycloakSession keycloakSession = keycloakSessionFactory.create()) {
                    SchedulerTaskProvider schedulerTaskProvider = create(keycloakSession);
                    ScheduledTask scheduledTask = schedulerTaskProvider.getScheduledTask();
                    TimerProvider timerProvider = keycloakSession.getProvider(TimerProvider.class);
                    timerProvider.scheduleTask(scheduledTask, 5000, getId());
                }
            }
        });
    }

    @Override
    public void close() {
        try (KeycloakSession keycloakSession = this.keycloakSessionFactory.create();) {
            keycloakSession.getProvider(TimerProvider.class).cancelTask(getId());
        }

    }
}
