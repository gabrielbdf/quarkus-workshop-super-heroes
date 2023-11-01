package io.quarkus.workshop.superheroes.fight.client;

import static jakarta.ws.rs.core.HttpHeaders.VARY;

import java.time.Instant;
import java.util.Date;

import io.quarkus.workshop.superheroes.fight.Fight;

public class DefaultTestFight extends Fight {

    public static final DefaultTestHero HERO = DefaultTestHero.INSTANCE;
    public static final DefaultTestVillain VILLAIN = DefaultTestVillain.INSTANCE;
    public static final DefaultTestFight INSTANCE = new DefaultTestFight();

    private DefaultTestFight() {
        this.fightDate = Instant.now();
        this.winnerName = HERO.name;
        this.winnerLevel = HERO.level;
        this.winnerPowers = HERO.powers;
        this.winnerPicture = HERO.picture;
        this.loserName = VILLAIN.name;
        this.loserLevel = VILLAIN.level;
        this.loserPowers = VILLAIN.powers;
        this.loserPicture = VILLAIN.picture;
        this.winnerTeam = "Hero";
        this.loserTeam = "Villain";

    }

}
