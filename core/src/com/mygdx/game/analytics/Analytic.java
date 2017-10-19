package com.mygdx.game.analytics;

public interface Analytic {

    public void gameOver(int level);

    public void startLevel(int level);

    public void nextLevel(int level);
}
