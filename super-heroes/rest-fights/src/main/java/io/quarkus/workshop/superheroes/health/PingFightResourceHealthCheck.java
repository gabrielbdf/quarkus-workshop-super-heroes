package io.quarkus.workshop.superheroes.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import io.quarkus.workshop.superheroes.fight.FightResource;
import jakarta.inject.Inject;

@Liveness
public class PingFightResourceHealthCheck implements HealthCheck {

    @Inject
    FightResource fightResource;

    @Override
    public HealthCheckResponse call() {
        String response = fightResource.hello();
        return HealthCheckResponse.named("PING to Fight REST endpoint").withData("Response", response).up().build();
    }

}
