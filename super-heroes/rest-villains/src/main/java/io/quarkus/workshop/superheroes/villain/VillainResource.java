package io.quarkus.workshop.superheroes.villain;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/villains")
public class VillainResource {

    @Inject
    Logger logger;
    @Inject
    VillainService service;

    @Operation(summary = "Returns a random villain")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/random")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    public RestResponse<Villain> getRandomVillain() {
        Villain villain = service.findRandomVillain();
        logger.debug("Found random villain " + villain);
        return RestResponse.ok(villain);
    }

    @Operation(summary = "Returns all villains")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY)))
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villains = service.findAllVillains();
        logger.debug("Total number of villains " + villains.size());
        return RestResponse.ok(villains);
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @APIResponse(responseCode = "204", description = "The villain is not found with id")
    public RestResponse<Villain> getVillain(@RestPath Long id) {
        Villain villain = service.findVillainById(id);
        if (villain != null) {
            logger.debug("Villain finded " + villain);
            return RestResponse.ok(villain);
        } else {
            logger.debug("No Villain finded with id" + id);
            return RestResponse.noContent();
        }
    }

    @Operation(summary = "Creates a valid villain")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "The URI of the created villain", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo
                .getAbsolutePathBuilder()
                .path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + builder.build().toString());
        return RestResponse.created(builder.build());
    }

    @Operation(summary = "Updated an existing villain")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "The update villain", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = service.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }

    @Operation(summary = "Delete an existing villain")
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "204", description = "Delete villain ")
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return RestResponse.noContent();
    }

    @GET
    @Path("/hello")
    @Tag(name = "Hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Villain Resource!";
    }

}
