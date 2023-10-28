package io.quarkus.workshop.superheroes.hero;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class HeroApplicationLifeCycle {

        private static final Logger LOGGER = Logger.getLogger(HeroApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("||  ||   |||||   ");
        LOGGER.info("||||||   |||     ");
        LOGGER.info("||  ||   |||||   ");
        LOGGER.info("The application HERO is starting with profile: " + ConfigUtils.getProfiles());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application HERO is stopping...");
    }
    
}
