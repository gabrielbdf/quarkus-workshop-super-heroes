package io.quarkus.workshop.superheroes.villain;

import java.util.List;

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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/random")
    public RestResponse<Villain> getRandomVillain() {
        Villain villain = service.findRandomVillain();
        logger.debug("Found random villain " + villain);
        return RestResponse.ok(villain);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villains = service.findAllVillains();
        logger.debug("Total number of villains " + villains.size());
        return RestResponse.ok(villains);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo
                .getAbsolutePathBuilder()
                .path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + builder.build().toString());
        return RestResponse.created(builder.build());
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = service.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return RestResponse.noContent();
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Villain Resource!";
    }

}
