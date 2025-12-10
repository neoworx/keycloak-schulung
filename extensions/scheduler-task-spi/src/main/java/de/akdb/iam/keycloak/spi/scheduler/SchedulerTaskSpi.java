package de.akdb.iam.keycloak.spi.scheduler;

import com.google.auto.service.AutoService;
import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

@AutoService(Spi.class)
public class SchedulerTaskSpi implements Spi {
    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public String getName() {
        return "akdb-scheduler-task-spi";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return SchedulerTaskProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return SchedulerTaskProviderFactory.class;
    }
}
