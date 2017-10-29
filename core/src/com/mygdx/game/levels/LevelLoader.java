package com.mygdx.game.levels;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.World;
import com.mygdx.game.levels.transformers.BlockTransformer;
import com.mygdx.game.levels.transformers.CoinTransformer;
import com.mygdx.game.levels.transformers.DiagonalEnemyTransformer;
import com.mygdx.game.levels.transformers.ExpandEnemyTransformer;
import com.mygdx.game.levels.transformers.HorizontalEnemyTransformer;
import com.mygdx.game.levels.transformers.PlayerTransformer;
import com.mygdx.game.levels.transformers.VerticalEnemyTransformer;
import com.mygdx.game.levels.transformers.WinTransformer;

import static com.mygdx.game.systems.RenderingSystem.CELL_TO_METERS;

public class LevelLoader {

    private final PlayerTransformer firstTransformer;
    private Pixmap pixmap;
    private PooledEngine engine;
    private World world;

    public LevelLoader(Pixmap pixmap, PooledEngine engine, World world) {
        this.pixmap = pixmap;
        this.engine = engine;
        this.world = world;
        this.firstTransformer = createFileTransformer(engine, world);
    }


    private PlayerTransformer createFileTransformer(PooledEngine engine, World world) {
        BlockTransformer blockTransformer = new BlockTransformer(engine, null);
        HorizontalEnemyTransformer horizontalEnemyTransformer = new HorizontalEnemyTransformer(engine, blockTransformer);
        VerticalEnemyTransformer verticalEnemyTransformer = new VerticalEnemyTransformer(engine, horizontalEnemyTransformer);
        DiagonalEnemyTransformer diagonalEnemyTransformer = new DiagonalEnemyTransformer(engine, verticalEnemyTransformer);
        CoinTransformer coinTransformer = new CoinTransformer(engine, diagonalEnemyTransformer);
        WinTransformer winTransformer = new WinTransformer(engine, coinTransformer);

        ExpandEnemyTransformer expandEnemyTransformer = new ExpandEnemyTransformer(engine, winTransformer);

        return new PlayerTransformer(engine, world, expandEnemyTransformer);
    }


    public void generateLevel() {
        final int width = pixmap.getWidth();
        final int height = pixmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = pixmap.getPixel(x, y);
                float posX = x * CELL_TO_METERS;
                float posY = y * CELL_TO_METERS;

                firstTransformer.create(posX, posY, pixel);
            }
        }
    }
}
