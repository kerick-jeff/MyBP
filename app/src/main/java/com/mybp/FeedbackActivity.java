package com.mybp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FeedbackActivity extends AppCompatActivity {
    private ImageView imageViewScreenShot;
    private Button buttonScreenShot;
    private TextView textView;
    //private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setupActionBar();

        buttonScreenShot = (Button) findViewById(R.id.buttonScreenShot);
        imageViewScreenShot = (ImageView) findViewById(R.id.imageViewScreenShot);
    }

    AppCompatActivity activity;

    public void onClickScreenShot(View view) {

        View view1 = getWindow().getDecorView().getRootView();
        view1.setDrawingCacheEnabled(true);
        Bitmap bitmap = view1.getDrawingCache();
        imageViewScreenShot.setImageBitmap(bitmap);
        //saveBitmap(bitmap);
    }

    private void saveBitmap(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "mybp_screenshot.png";
        File screenShot = new File(path);
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = new FileOutputStream(screenShot);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException ex) {
            Toast.makeText(FeedbackActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:

                return true;
            default: return super.onContextItemSelected(item);
        }
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
}
