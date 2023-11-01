package io.quarkus.workshop.superheroes.fight.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.workshop.superheroes.fight.Fight;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/narration")
@Produces(MediaType.TEXT_PLAIN)
@RegisterRestClient(configKey = "narration")
public interface NarrationProxy {

    @POST
    String narrate(Fight fight);

}
