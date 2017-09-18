package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    public static TextureRegion playGame, enemy, background, coin, player, mainMenu, pauseMenu, ready, gameOver, soundOn, soundOff, pause;

    public static BitmapFont font;
    public static Texture logo;

    public static Texture items;

    public static Animation bobJump;
    public static Animation bobFall;
    public static Animation bobHit;


    public static Music music;
    public static Sound deadSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {
        player = new TextureRegion(loadTexture("data/player.png"));
        enemy = new TextureRegion(loadTexture("data/enemy.png"));
        coin = new TextureRegion(loadTexture("data/coin.png"));
        playGame = new TextureRegion(loadTexture("data/playgame.jpg"));
        background = new TextureRegion(loadTexture("data/level1.png"));
        items = loadTexture("data/items.png");
        mainMenu = new TextureRegion(loadTexture ("data/menu.jpg"));
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(loadTexture("data/gameover.png"));
        pause = new TextureRegion(items, 64, 64, 64, 64);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
        clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        if (Settings.soundEnabled) music.play();

        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        deadSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
    }

    public static void playSound (Sound sound) {
        if (Settings.soundEnabled) sound.play(1);
    }
}