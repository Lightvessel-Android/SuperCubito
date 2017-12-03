package adictive.games.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AssetsUtils {
    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
}
