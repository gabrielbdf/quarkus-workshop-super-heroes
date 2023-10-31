package io.quarkus.workshop.superheroes.narration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class NarrationResourceTest {

    private static final String HERO_NAME = "Super Baguette";
    private static final int HERO_LEVEL = 42;
    private static final String HERO_POWERS = "Eats baguette in less than a second";
    private static final String VILLAIN_NAME = "Super Chocolatine";
    private static final int VILLAIN_LEVEL = 43;
    private static final String VILLAIN_POWERS = "Transforms chocolatine into pain au chocolat";

    @InjectMock
    NarrationService narrationService;

    private static Fight getFight() {
        Fight fight = new Fight();
        fight.winnerName = VILLAIN_NAME;
        fight.winnerLevel = VILLAIN_LEVEL;
        fight.winnerPowers = VILLAIN_POWERS;
        fight.loserName = HERO_NAME;
        fight.loserLevel = HERO_LEVEL;
        fight.loserPowers = HERO_POWERS;
        fight.winnerTeam = "villains";
        fight.loserTeam = "heroes";
        return fight;
    }

    @BeforeEach
    public void setup() throws Exception {
        Mockito.when(narrationService.narrate(getFight())).thenReturn("Lorem ipsum dolor site");
    }

    @Test
    void shouldPingOpenAPI() {
        given()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get("/q/openapi")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/api/narration/hello")
                .then()
                .statusCode(200)
                .body(is("Hello Narration Resource"));
    }

    @Test
    void shouldNarrateAFight() {
        given().log().all()
                .body(getFight())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN)
                .when()
                .post("/api/narration")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
        // .body(startsWith("Lorem ipsum dolor sit amet"));
    }

}