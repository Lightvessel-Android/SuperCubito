package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import adictive.games.SquareWorld;
import adictive.games.components.CoinComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;

public class LevelLoader {

    private final String fileName;
    private SquareWorld world;
    private Engine engine;

    public LevelLoader(int levelNumber, SquareWorld world, Engine engine) {
        this.fileName = "level" + levelNumber + ".txt";
        this.world = world;
        this.engine = engine;
    }

    public void load() {
        FileHandle fileHandle = Gdx.files.internal("levels/" + fileName);
        String s = fileHandle.readString();
        String[] lines = s.split("\n");
        for (String line : lines) {
            String[] entity = line.split(",");
            switch (entity[0]) {
                case "Block":
                    parseBlock(entity);
                    break;
                case "Enemy":
                    parseEnemy(entity);
                    break;
                case "Player":
                    parsePlayer(entity);
                    break;
                case "Win":
                    parseWin(entity);
                    break;
                case "Coin":
                    parseCoin(entity);
                    break;
            }
        }
    }

    private void parseCoin(String[] line) {
        CoinComponent.addNew(
                engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parseWin(String[] line) {
        WinComponent.addNew(
                engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parsePlayer(String[] line) {
        PlayerComponent.addNew(
                world, engine,
                Float.parseFloat(line[1]), Float.parseFloat(line[2])
        );
    }

    private void parseEnemy(String[] line) {
        EnemyComponent.addNew(
                engine, 0, 0,
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3]),
                Float.parseFloat(line[4]),
                Float.parseFloat(line[5])
        );
    }

    private void parseBlock(String[] line) {
        loadBlock(world, engine, Integer.parseInt(line[1]), Integer.parseInt(line[2]));
    }

    private static void loadBlock(SquareWorld world, Engine engine, int x, int y) {
        WallComponent.addNew(engine, x, y);
    }
}
