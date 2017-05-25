package com.mybp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {
    private final int DURATION = 3000;
    private Thread thread;
    private ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);
        loadDataFromAsset();

        thread = new Thread(){
            @Override
            public void run(){
                synchronized (this) {
                    try {
                        wait(DURATION);
                    } catch (InterruptedException ex) {

                    } finally {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

        thread.start();
    }

    private void loadDataFromAsset(){
        try {
            InputStream inputStream = getAssets().open("icon.png");
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageViewIcon.setImageDrawable(drawable);
        } catch (IOException ex) {
            return;
        }
    }
}
