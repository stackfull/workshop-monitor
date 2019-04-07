package com.stackfull.workshop.monitor.mock;

import java.util.concurrent.ThreadLocalRandom;

class RandomChoice {
    private final String[] choices;

    RandomChoice(String[] choices) {
        this.choices = choices;
    }

    String pick() {
        int choice = ThreadLocalRandom.current().nextInt(choices.length);
        return choices[choice];
    }


}
