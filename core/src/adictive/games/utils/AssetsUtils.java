package adictive.games.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by arielalvarez on 12/3/17.
 */

public class AssetsUtils {
    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }
}
