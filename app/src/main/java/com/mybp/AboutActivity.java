package com.mybp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {
    private ImageView imageViewIcon;
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabFacebook, fabTwitter, fabEmail;
    View viewDimmer;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupActionBar();

        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);

        loadDataFromAsset();

        /* Setup Floating action button */
        viewDimmer = (View) findViewById(R.id.viewDimmer);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

        viewDimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.close(true);
            }
        });

        fabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened) { // build FloatingActionMenu & FloatingActionButton(s) dynamically at runtime
                    fabFacebook = new FloatingActionButton(AboutActivity.this);
                    fabFacebook.setLabelText(getResources().getString(R.string.fab_facebook));
                    fabFacebook.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_facebook));
                    fabFacebook.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabFacebook.setColorNormal(getResources().getColor(android.R.color.white));
                    fabFacebook.setColorPressed(getResources().getColor(R.color.gray_light));
                    fabFacebook.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabFacebook.setShowShadow(true);
                    fabFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/2314"));
                                startActivity(intent);
                            } catch (Exception ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MyBP"));
                                startActivity(intent);
                            }

                            fabMenu.close(true);
                        }
                    });

                    fabTwitter = new FloatingActionButton(AboutActivity.this);
                    fabTwitter.setLabelText(getResources().getString(R.string.fab_twitter));
                    fabTwitter.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_twitter));
                    fabTwitter.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabTwitter.setColorNormal(getResources().getColor(android.R.color.white));
                    fabTwitter.setColorPressed(getResources().getColor(R.color.gray_light));
                    fabTwitter.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabTwitter.setShowShadow(true);
                    fabTwitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fabMenu.close(true);
                        }
                    });

                    fabEmail = new FloatingActionButton(AboutActivity.this);
                    fabEmail.setLabelText(getResources().getString(R.string.fab_email));
                    fabEmail.setImageDrawable(getResources().getDrawable(R.drawable.ic_mail_outline_red_400_24dp));
                    fabEmail.setButtonSize(FloatingActionButton.SIZE_MINI);
                    fabEmail.setColorNormal(getResources().getColor(android.R.color.white));
                    fabEmail.setColorPressed(getResources().getColor(R.color.gray_light));
                    fabEmail.setColorRipple(getResources().getColor(R.color.fab_ripple));
                    fabEmail.setShowShadow(true);
                    fabEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:" + "frukerickjeff@gmail.com"));

                            try {
                                startActivity(Intent.createChooser(intent, "Contact us through..."));
                            } catch (ActivityNotFoundException ex) {
                                Toast.makeText(AboutActivity.this, "No email clients installed", Toast.LENGTH_LONG).show();
                            }

                            fabMenu.close(true);
                        }
                    });

                    fabMenu.addMenuButton(fabFacebook);
                    fabMenu.addMenuButton(fabTwitter);
                    fabMenu.addMenuButton(fabEmail);
                } else {
                    fabMenu.removeAllMenuButtons();
                }

                setBackgroundDimming(opened);
            }

            private void setBackgroundDimming(boolean dimmed) {
                final float targetAlpha = dimmed ? 1f : 0;
                final int endVisibility = dimmed ? View.VISIBLE : View.GONE;
                viewDimmer.setVisibility(View.VISIBLE);
                viewDimmer.animate()
                        .alpha(targetAlpha)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                viewDimmer.setVisibility(endVisibility);
                            }
                        })
                        .start();
            }
        });

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fabMenu.isOpened()) {
                    fabMenu.close(true);
                }
            }
        });
        /* End fab setup */
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
}
