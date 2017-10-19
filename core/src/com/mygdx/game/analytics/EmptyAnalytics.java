package com.mygdx.game.analytics;

public class EmptyAnalytics implements Analytic {
    @Override
    public void gameOver(int level) {
        System.out.println("game over level: " + level);
    }

    @Override
    public void startLevel(int level) {
        System.out.println("startlevel: " + level);
    }

    @Override
    public void nextLevel(int level) {
        System.out.println("nextLevel: " + level);
    }
}
