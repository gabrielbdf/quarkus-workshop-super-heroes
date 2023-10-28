package io.quarkus.workshop.superheroes.hero;

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

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

@Path("/api/heroes")
@Tag(name = "heroes")
public class HeroResource {

    @Inject
    Logger logger;

    @Operation(summary = "Returns a random hero")
    @GET
    @Path("/random")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class, required = true)))
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<Hero>> getRandomHero() {
        return Hero.findRandom()
                .onItem().ifNotNull().transform(h -> {
                    this.logger.debugf("found random hero: %s", h);
                    return RestResponse.ok(h);
                });
    }

    @Operation(summary = "Returns all the heroes from the database")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class, type = SchemaType.ARRAY)))
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Hero>> getAllHeroes() {
        return Hero.listAll();
    }

    @Operation(summary = "Returns a hero for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    @APIResponse(responseCode = "204", description = "The hero is not found for a given identifier")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<Hero>> getHero(@RestPath Long id) { 
        return Hero.<Hero>findById(id)
                .map(hero -> {
                    if (hero != null) {
                        logger.debugf("found hero by id: %s", id);
                        return RestResponse.ok(hero);
                    } else {
                        logger.debugf("hero not found by id: %s", id);
                        return RestResponse.noContent();
                    }
                });
    }

    @Operation(summary = "Creates a valid hero")
    @POST
    @APIResponse(responseCode = "201", description = "The URI of the created hero", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @WithTransaction
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<URI>> createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
        return hero.<Hero>persist()
                .map(h -> {
                    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(h.id));
                    logger.debug("Hero created with uri " + builder.build().toString());
                    return RestResponse.created(builder.build());
                });
    }

    @Operation(summary = "Updates an exiting hero")
    @PUT
    @APIResponse(responseCode = "200", description = "The updated hero", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    @WithTransaction
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Hero> updateHero(@Valid Hero hero) {
        return Hero.<Hero>findById(hero.id)
                .map(retrieved -> {
                    retrieved.name = hero.name;
                    retrieved.level = hero.level;
                    retrieved.otherName = hero.otherName;
                    retrieved.picture = hero.picture;
                    retrieved.powers = hero.powers;
                    return retrieved;
                }).map(h -> {
                    logger.debugf("Hero updated with new valued %s", h);
                    return h;
                });

    }

    @Operation(summary = "Deletes an exiting hero")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    @WithTransaction
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<Void>> deleteHero(@RestPath Long id) {
        return Hero
                .deleteById(id)
                .invoke(() -> logger.debugf("Hero deleted with id %d", id))
                .replaceWith(RestResponse.noContent()); 
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello Hero Resource";
    }
}
