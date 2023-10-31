package io.quarkus.workshop.superheroes.narration;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/narration")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "narration")
public class NarrationResource {

    @Inject
    NarrationService service;

    @POST
    public Response narrate(Fight fight) throws Exception {
        String narration = service.narrate(fight);
        return Response.status(Response.Status.CREATED).entity(narration).build();
    }

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Narration Resource";
    }
}
