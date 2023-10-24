package io.quarkus.workshop.superheroes.fight.client.proxy;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.test.Mock;
import io.quarkus.workshop.superheroes.fight.client.DefaultTestHero;
import io.quarkus.workshop.superheroes.fight.client.Hero;
import io.quarkus.workshop.superheroes.fight.client.HeroProxy;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
@RestClient
public class MockHeroProxy implements HeroProxy {

    @Override
    public Hero findRandomHero() {
        return DefaultTestHero.INSTANCE;
    }

}
