package com.mybp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {
    private ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupActionBar();

        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);

        loadDataFromAsset();
    }

    private void setupActionBar() {
        // Set action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

    public void facebookLike(View view) {

    }

    public void emailUs(View view) {

    }
}
