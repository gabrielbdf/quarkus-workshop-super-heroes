package io.quarkus.workshop.superheroes.fight;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/fights")
@Tag(name = "Fight")
public class FightResource {

    @Inject
    Logger logger;

    @Inject
    FightService service;

    @ConfigProperty(name = "process.milliseconds", defaultValue = "0")
    long tooManyMilliseconds;

    private void veryLongProcess() {
        try {
            Thread.sleep(tooManyMilliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Operation(description = "Select random fighters")
    @GET
    @Path("/randomfighters")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Fighters.class)))
    public Response getRandomFighters() {
        veryLongProcess();
        Fighters fighters = service.findRandomFighters();
        logger.debug("Get random fighters " + fighters);
        return Response.ok(fighters).build();
    }

    @Operation(description = "Select all fights")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Fight.class, type = SchemaType.ARRAY)))
    public Response findAllFights() {
        List<Fight> fights = service.findAllFights();
        return Response.ok(fights).build();
    }

    @Operation(description = "Select fight by id")
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Fight.class)))
    @APIResponse(responseCode = "204", description = "The villain is not found with id")
    public Response getFight(Long id) {
        Fight fight = service.findFightById(id);
        if (fight != null) {
            logger.debug("Found fight " + fight);
            return Response.ok(fight).build();
        } else {
            logger.debug("No fight found with id " + id);
            return Response.noContent().build();
        }
    }

    @Operation(description = "Create a fight")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "Create fight", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Fight.class)))
    @POST
    public Fight fight(@Valid Fighters fighters, UriInfo uriInfo) {
        return service.persistFight(fighters);
    }

    @POST
    @Path("/narrate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response narrateFight(@Valid Fight fight) {
        logger.debug("Narrate the fight " + fight);
        String narration = service.narrateFight(fight);
        return Response.status(Response.Status.CREATED).entity(narration).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    @Tag(name = "Hello")
    public String hello() {
        return "Hello Fight Resource";
    }
}
