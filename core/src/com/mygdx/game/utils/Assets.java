package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static TextureRegion playGame, winBlock, wallblock, enemy, background, coin, player, mainMenu, pauseMenu, ready, gameOver, soundOn, soundOff, pause, unLockedLevel, lockedLevel;

    public static BitmapFont font, yellowFont;

    public static List<Pixmap> levels = new ArrayList<Pixmap>();

    public static Texture items, backgroundTexture;

    public static TextButton.TextButtonStyle lockedStyle, unLockedStyle;

    public static Skin skin;

    public static Music music;
    public static Sound deadSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        lockedLevel = new TextureRegion(loadTexture("data/buttonLocked.png"));
        unLockedLevel = new TextureRegion(loadTexture("data/button.png"));
        wallblock = new TextureRegion(loadTexture("data/blackBox.png"));
        winBlock = new TextureRegion(loadTexture("data/winBox.png"));
        player = new TextureRegion(loadTexture("data/player.png"));
        enemy = new TextureRegion(loadTexture("data/enemy.png"));
        coin = new TextureRegion(loadTexture("data/coin.png"));
        playGame = new TextureRegion(loadTexture("data/playgame.jpg"));

        backgroundTexture = loadTexture("data/background.png");
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new TextureRegion(backgroundTexture);

        items = loadTexture("data/items.png");
        mainMenu = new TextureRegion(loadTexture ("data/menu.jpg"));
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(loadTexture("data/gameover.png"));
        pause = new TextureRegion(items, 64, 64, 64, 64);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/Walkway/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        parameter.color = Color.NAVY;
        yellowFont = generator.generateFont(parameter);
        generator.dispose();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled) music.play();

        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        deadSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));



        //Button styles
        lockedStyle = new TextButton.TextButtonStyle();
        lockedStyle.up = new TextureRegionDrawable(lockedLevel);
        lockedStyle.down = new TextureRegionDrawable(lockedLevel);
        lockedStyle.over = new TextureRegionDrawable(lockedLevel);
        lockedStyle.pressedOffsetX = 1;
        lockedStyle.pressedOffsetY = -1;
        lockedStyle.font = font;

        unLockedStyle = new TextButton.TextButtonStyle();
        unLockedStyle.up = new TextureRegionDrawable(unLockedLevel);
        unLockedStyle.down = new TextureRegionDrawable(unLockedLevel);
        unLockedStyle.over = new TextureRegionDrawable(unLockedLevel);
        unLockedStyle.pressedOffsetX = 1;
        unLockedStyle.pressedOffsetY = -1;
        unLockedStyle.font = font;


        //Levels:
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level-2.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level-1.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level0.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level1.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level2.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level3.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level4.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level5.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level6.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level7.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level8.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level9.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level10.bmp")));


        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));
        levels.add(new Pixmap(Gdx.files.internal("bitmaps/level.bmp")));

    }

    public static void playSound (Sound sound) {
        if (Settings.soundEnabled) sound.play(1);
    }

    public static Pixmap getLevel(int level){
        return levels.get(level);
    }
}