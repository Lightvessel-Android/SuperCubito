package com.mygdx.game.ads;

import android.widget.Toast;

import com.mygdx.game.AndroidLauncher;

public class InterstitialAd implements AdInterface {

    private AndroidLauncher context;

    public InterstitialAd(AndroidLauncher context){
        this.context = context;
    }

    @Override
    public void showAd() {

        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, "PERDISTE", Toast.LENGTH_LONG).show();
            }
        });
    }
}
