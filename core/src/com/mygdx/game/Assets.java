package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    public static Texture playGame;
    public static Texture background;
    public static Texture items;
    public static Texture mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static Texture gameOver;
    public static BitmapFont font;
    public static Texture logo;
    public static TextureRegion pause;

    public static TextureRegion soundOn;
    public static TextureRegion soundOff;

//    public static TextureRegion backgroundRegion;
//    public static TextureRegion highScoresRegion;
//    public static TextureRegion arrow;
//    public static TextureRegion spring;
//    public static TextureRegion castle;
//    public static Animation coinAnim;
//    public static Animation bobJump;
//    public static Animation bobFall;
//    public static Animation bobHit;
//    public static Animation squirrelFly;
//    public static Animation platform;
//    public static Animation breakingPlatform;


    public static Music music;
//    public static Sound jumpSound;
//    public static Sound highJumpSound;
//    public static Sound hitSound;
//    public static Sound coinSound;
    public static Sound clickSound;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {

        //Aca se cargan todas las texturas con sus posiciones.
        playGame = loadTexture("data/playgame.jpg");
        background = loadTexture("data/level1.png");
        items = loadTexture("data/items.png");
        mainMenu = loadTexture ("data/menu.jpg");
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = loadTexture("data/gameover.png");
        logo = loadTexture("data/menu.jpg");
        pause = new TextureRegion(items, 64, 64, 64, 64);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);

//        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
//        highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
//        arrow = new TextureRegion(items, 0, 64, 64, 64);
//
//        spring = new TextureRegion(items, 128, 0, 32, 32);
//        castle = new TextureRegion(items, 128, 64, 64, 64);
//        coinAnim = new Animation(0.2f, new TextureRegion(items, 128, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32),
//                new TextureRegion(items, 192, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32));
//        bobJump = new Animation(0.2f, new TextureRegion(items, 0, 128, 32, 32), new TextureRegion(items, 32, 128, 32, 32));
//        bobFall = new Animation(0.2f, new TextureRegion(items, 64, 128, 32, 32), new TextureRegion(items, 96, 128, 32, 32));
//        bobHit = new Animation(0.2f, new TextureRegion(items, 128, 128, 32, 32));
//        squirrelFly = new Animation(0.2f, new TextureRegion(items, 0, 160, 32, 32), new TextureRegion(items, 32, 160, 32, 32));
//        platform = new Animation(0.2f, new TextureRegion(items, 64, 160, 64, 16));
//        breakingPlatform = new Animation(0.2f, new TextureRegion(items, 64, 160, 64, 16), new TextureRegion(items, 64, 176, 64, 16),
//                new TextureRegion(items, 64, 192, 64, 16), new TextureRegion(items, 64, 208, 64, 16));
//

//

//        music.setLooping(true);
//        music.setVolume(0.5f);
//        if (Settings.soundEnabled) music.play();
//        jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
//        highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
//        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
//        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
//
//        coinAnim.setPlayMode(PlayMode.LOOP);
//        bobJump.setPlayMode(PlayMode.LOOP);
//        bobFall.setPlayMode(PlayMode.LOOP);
//        bobHit.setPlayMode(PlayMode.LOOP);
//        squirrelFly.setPlayMode(PlayMode.LOOP);
//        platform.setPlayMode(PlayMode.LOOP);
    }

    public static void playSound (Sound sound) {
        if (Settings.soundEnabled) sound.play(1);
    }
}