package adictive.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import adictive.games.play.PlayScreen;
import adictive.games.utils.GameData;

public class SquareGame extends Game {
    public SpriteBatch batcher;
    private FPSLogger logger;

    private int level = 0;

    public SquareGame() {
        logger = new FPSLogger();
    }

    @Override
    public void create() {
        batcher = new SpriteBatch();
        goToLevel(GameData.getCurrentLevel());
    }

    @Override
    public void render() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0xFF/255f, 0xFC/255f, 0xEF/255f, 1.0f);
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
