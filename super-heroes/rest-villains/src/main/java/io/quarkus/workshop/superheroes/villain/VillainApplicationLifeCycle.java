package io.quarkus.workshop.superheroes.villain;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class VillainApplicationLifeCycle {

    private static final Logger LOGGER = Logger.getLogger(VillainApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("\\______ \\ _____ _______|  | __ \\   \\ /   /|__| /  _  \\ ______ |__| ");
        LOGGER.info("  |    |  \\__  \\_  __ \\  |/ /  \\   Y   / |  |/  /_\\  \\____ \\|  |  ");
        LOGGER.info("  |    `   \\/ __ \\|  | \\/    <  \\     /  |  /    |    \\  |_> >  | ");
        LOGGER.info("   /_______  (____  /__|  |__|_ \\  \\___/   |__\\____|__  /   __/|__| ");
        LOGGER.info("The application VILLAIN is starting with profile: " + ConfigUtils.getProfiles());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application VILLAIN is stopping...");
    }

}
