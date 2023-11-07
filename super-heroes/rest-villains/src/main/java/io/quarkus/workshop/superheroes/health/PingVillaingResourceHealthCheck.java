package io.quarkus.workshop.superheroes.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import io.quarkus.workshop.superheroes.villain.VillainResource;
import jakarta.inject.Inject;

@Liveness
public class PingVillaingResourceHealthCheck implements HealthCheck {

    @Inject
    VillainResource villainResource;

    @Override
    public HealthCheckResponse call() {
        String response = villainResource.hello();
        return HealthCheckResponse.named("Ping to Hillain REST endpoint").withData("Response", response).up().build();
    }

}
