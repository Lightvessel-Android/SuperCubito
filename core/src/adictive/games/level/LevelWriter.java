package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;

public class LevelWriter {
    private final String fileName;

    public LevelWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(final Engine engine) {
        PrintWriter writer;
        try {
            writer = new PrintWriter("levels/" + fileName, "UTF-8");
            ImmutableArray<Entity> entities = engine.getEntities();
            for (Entity e : entities) {
                if (e.getComponent(WallComponent.class) != null) {
                    writer.println(blockToCSV(e));
                } else if (e.getComponent(EnemyComponent.class) != null) {
                    writer.println(enemyToCSV(e));
                } else if (e.getComponent(PlayerComponent.class) != null) {
                    writer.println(playerToCSV(e));
                }
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String playerToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Player",tc.pos.x, tc.pos.y) ;
    }

    public String enemyToCSV(Entity e) {
        EnemyComponent ec = e.getComponent(EnemyComponent.class);
        return csv("Enemy",ec.initialPosInLine,ec.start.x,ec.start.y,ec.end.x,ec.end.y) ;
    }

    public String blockToCSV(Entity e) {
        TransformComponent tc = e.getComponent(TransformComponent.class);
        return csv("Block", (int)tc.pos.x, (int)tc.pos.y);
    }

    private static String csv(Object... elements) {
        final StringBuilder sb = new StringBuilder();
        for (Object s : elements) {
            sb.append(s.toString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
