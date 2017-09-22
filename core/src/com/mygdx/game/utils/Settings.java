package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {

    public static boolean soundEnabled = true;

    public static int levelMax = 1;

    public final static String file = ".supercubito";

    public static void load () {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            String[] strings = filehandle.readString().split("\n");

            soundEnabled = Boolean.parseBoolean(strings[0]);

            levelMax = Integer.parseInt(strings[1]);

        } catch (Throwable e) {
        }
    }

    public static void save () {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            filehandle.writeString(Integer.toString(levelMax)+"\n", true);
        } catch (Throwable e) {
        }
    }


}
