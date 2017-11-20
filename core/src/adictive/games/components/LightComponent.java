package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.mygdx.game.utils.Assets.loadTexture;

public class LightComponent implements Component {
    public static final TextureRegion CIRCULAR = new TextureRegion(loadTexture("data/light_circular.png"));
    public static final TextureRegion SQUARE = new TextureRegion(loadTexture("data/light_square.png"));
    public Color color = new Color();
    public float radius = 2f;
    public TextureRegion shape = CIRCULAR;
}
