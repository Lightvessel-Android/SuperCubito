package adictive.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ads.AdInterface;
import com.mygdx.game.analytics.Analytic;
import com.mygdx.game.utils.Settings;

import adictive.games.play.PlayScreen;

public class SquareGame extends Game {
    public SpriteBatch batcher;
    public final AdInterface adInterface;
    public final Analytic analytic;
    private FPSLogger logger;

    private int level = 0;

    public SquareGame(AdInterface adInterface, Analytic analytic) {
        this.adInterface = adInterface;
        this.analytic = analytic;
        logger = new FPSLogger();
    }

    @Override
    public void create() {
        batcher = new SpriteBatch();
        Settings.load();
        com.mygdx.game.utils.Assets.load();
        goToNextLevel();
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0xF4/255f, 0xF2/255f, 0xE6/255f, 1.0f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        logger.log();
        super.render();
    }

    public void goToLevel(int level) {
        this.level = level;
        setScreen(new PlayScreen(this, level));
    }

    public void goToNextLevel() {
        goToLevel(++level);
    }

    public void goToPrevLevel() {
        if (level > 1) goToLevel(--level);
    }
}
