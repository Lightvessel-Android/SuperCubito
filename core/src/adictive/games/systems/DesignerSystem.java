package adictive.games.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import adictive.games.SquareWorld;
import adictive.games.components.BoundsComponent;
import adictive.games.components.CoinComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;
import adictive.games.components.WinComponent;
import adictive.games.level.LevelWriter;
import adictive.games.play.PlayScreen;

public class DesignerSystem extends EntitySystem {

    private static final int NAVIGATION_MODE = 0;
    private static final int ENEMY_END_POSITION_MODE = 2;
    private static final int ENEMY_INITIAL_POSITION_MODE = 3;

    private static final Vector3 UP    = new Vector3( 0f,  1f, 0f);
    private static final Vector3 DOWN  = new Vector3( 0f, -1f, 0f);
    private static final Vector3 LEFT  = new Vector3(-1f,  0f, 0f);
    private static final Vector3 RIGHT = new Vector3( 1f,  0f, 0f);

    private final SquareWorld world;
    private PlayScreen screen;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final Family wallFamily = Family.all(WallComponent.class, BoundsComponent.class, TransformComponent.class).get();
    private final Family block = Family.all(WallComponent.class, WinComponent.class, TransformComponent.class).get();
    private final Family transformable = Family.all(BoundsComponent.class, TransformComponent.class).get();
    private final Family enemiesFamily = Family.all(EnemyComponent.class).get();

    private final Vector3 lastTouch = new Vector3(0,0,0);
    private final Vector3 touch = new Vector3(0,0,0);
    private final Vector3 cursor = new Vector3(0,0,0);

    private Entity enemy;
    private int editionMode = NAVIGATION_MODE;

    public DesignerSystem(SquareWorld world, PlayScreen screen) {
        super(11);
        this.world = world;
        this.screen = screen;
    }

    @Override
    public void update(float deltaTime) {
        updateCameraPosition();
        editEntities();
        drawGrid();
        drawHelpers();
        saveLevel();
    }

    private void saveLevel() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            new LevelWriter(screen.level).write(getEngine());
        }
    }

    private static final Color LIGHT_RED = new Color(0xffa3b7ff);

    private void drawHelpers() {
        shapeRenderer.setProjectionMatrix(world.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.FIREBRICK);

        if (editionMode == ENEMY_END_POSITION_MODE) {
            final EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
            shapeRenderer.line(ec.start.x, ec.start.y, cursor.x, cursor.y);
        } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
            final EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
            shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y);
        }

        ImmutableArray<Entity> enemies = getEngine().getEntitiesFor(enemiesFamily);
        shapeRenderer.setColor(LIGHT_RED);
        for (Entity e: enemies) {
            if (e != enemy) {
                final EnemyComponent ec = e.getComponent(EnemyComponent.class);
                shapeRenderer.line(ec.start.x, ec.start.y, ec.end.x, ec.end.y);
            }
        }

        shapeRenderer.end();
    }

    private void drawGrid() {

        shapeRenderer.setProjectionMatrix(world.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x < world.getWidth(); x++) {
            shapeRenderer.line(x, 0, x, world.getHeight());
        }

        for (int y = 0; y < world.getHeight(); y++) {
            shapeRenderer.line(0, y, world.getWidth(), y);
        }

        shapeRenderer.end();
    }

    private void editEntities() {

        cursor.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        world.getCamera().unproject(cursor);

        if(Gdx.input.justTouched()) {
            lastTouch.set(cursor);
        }

        if (editionMode == NAVIGATION_MODE) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                createEnemy();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
                createPlayer();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.C)){
                createCoin();
            }
        } else if (editionMode == ENEMY_END_POSITION_MODE && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
            ec.end.set(cursor.x, cursor.y);
            editionMode = ENEMY_INITIAL_POSITION_MODE;
        } else if (editionMode == ENEMY_INITIAL_POSITION_MODE) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                ec.resetDirection();
                EnemySystem.moveEnemyToPos(enemy, ec, ec.posInLine - 0.5f);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                EnemyComponent ec = enemy.getComponent(EnemyComponent.class);
                ec.resetDirection();
                EnemySystem.moveEnemyToPos(enemy, ec, ec.posInLine + 0.5f);
                ec.initialPosInLine = ec.posInLine + 0.5f;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                enemy = null;
                editionMode = NAVIGATION_MODE;
            }
        }

        if(Gdx.input.isTouched()) {
            touch.set(cursor);

            if (editionMode == NAVIGATION_MODE) {
                if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                    createWall();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                    removeEntity();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                    createWinBlock();
                }

            }
        }
    }

    private void createCoin() {
        CoinComponent.createCoin(getEngine(), cursor.x - CoinComponent.WIDTH/2, cursor.y - CoinComponent.WIDTH/2);
    }

    private void createWinBlock() {
        final int x = (int)touch.x;
        final int y = (int)touch.y;

        if (getEngine().getSystem(CollisionSystem.class).entityMap[x][y][CollisionSystem.WIN] != null) return;

        WinComponent.createWinComponent(getEngine(), cursor.x, cursor.y);
    }

    private void createPlayer() {
        PlayerComponent.createPlayer(world, getEngine(), cursor.x- PlayerComponent.WIDTH/2, cursor.y - PlayerComponent.WIDTH/2);
    }

    private void createEnemy() {
        this.enemy = EnemyComponent.createEnemy(getEngine(), cursor.x, cursor.y);
        enemy.getComponent(EnemyComponent.class).start.set(cursor.x, cursor.y);
        editionMode = ENEMY_END_POSITION_MODE;
    }

    private void removeEntity() {
        final ImmutableArray<Entity> entities = getEngine().getEntitiesFor(transformable);
        final Rectangle body = new Rectangle();
        for (Entity entity : entities) {
            final TransformComponent tc = entity.getComponent(TransformComponent.class);
            body.set(tc.pos.x, tc.pos.y, tc.size.x, tc.size.y);

            if (body.contains(touch.x, touch.y)) {
                getEngine().removeEntity(entity);
            }
        }
    }

    private void createWall() {

        final int x = (int)touch.x;
        final int y = (int)touch.y;

        if (getEngine().getSystem(CollisionSystem.class).entityMap[x][y][CollisionSystem.WALL] != null) return;

        WallComponent.createBlock(getEngine(), x, y);
    }


    private void updateCameraPosition() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            world.getCamera().position.add(UP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            world.getCamera().position.add(DOWN);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            world.getCamera().position.add(LEFT);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            world.getCamera().position.add(RIGHT);
        }
    }

}
