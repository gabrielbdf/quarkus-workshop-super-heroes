package io.quarkus.workshop.superheroes.statistics;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Score {

    public String name;
    public Integer score;

    public Score() {
        this.score = 0;
    }
}
