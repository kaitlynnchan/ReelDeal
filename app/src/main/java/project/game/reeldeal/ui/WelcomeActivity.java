package project.game.reeldeal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import project.game.reeldeal.R;

/**
 * Welcome Screen
 * Plays animation and allows users to be able to
 *  skip the welcome animation and go to main menu
 */
public class WelcomeActivity extends AppCompatActivity {

    private static final String TRANSLATION_Y = "translationY";
    private static final String TRANSLATION_X = "translationX";

    private Handler handler = new Handler();
    private int heightScreen;
    private int widthScreen;
    private int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        titleWelcomeAnimation();
        setupSkipButton();

        handler.postDelayed(new Runnable() {
            @Override public void run() {
                launchMainActivity();
            }
        }, timer);
    }

    private void titleWelcomeAnimation() {
        // Fishing pole animations
        ImageView imgFishingPole = findViewById(R.id.image_fishing_pole);
        Animation rotate = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.anim_rotate);
        imgFishingPole.startAnimation(rotate);

        timer += 3500;

        // Game title animations
        TextView gameTitle = findViewById(R.id.text_title);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle, TRANSLATION_Y,
                (heightScreen / -7f) * 2);
        moveY.setStartDelay(timer);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle, TRANSLATION_X,
                widthScreen / 5f);
        moveX.setStartDelay(timer);
        moveX.setDuration(2000);
        moveX.start();

        timer += 2000 + 500;
        animateAfterFishing();
    }

    private void animateAfterFishing() {
        // Black overlay animation
        View overlay = findViewById(R.id.view_overlay);
        overlay.setVisibility(View.VISIBLE);
        ViewPropertyAnimator fadeIn = overlay.animate();
        fadeIn.alpha(0.5f);
        fadeIn.setStartDelay(timer);
        fadeIn.setDuration(1000);

        // Game title animations
        TextView gameName = findViewById(R.id.text_title);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameName, TRANSLATION_Y,
                heightScreen / -3f);
        moveY.setStartDelay(timer);
        moveY.setDuration(1000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameName, TRANSLATION_X,
                widthScreen / 9f);
        moveX.setStartDelay(timer);
        moveX.setDuration(1000);
        moveX.start();

        Animation scaleZoom = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.anim_zoom);
        scaleZoom.setStartOffset(timer);
        scaleZoom.setFillAfter(true);
        gameName.startAnimation(scaleZoom);

        // Author animation
        TextView authors = findViewById(R.id.text_authors);
        ObjectAnimator moveAuthorX = ObjectAnimator.ofFloat(authors, TRANSLATION_X,
                (widthScreen / 3f) * 2);
        moveAuthorX.setStartDelay(timer);
        moveAuthorX.setDuration(1000);
        moveAuthorX.start();

        timer += 1000 + 4000;
    }

    private void setupSkipButton() {
        Button buttonSkip = findViewById(R.id.button_skip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                launchMainActivity();
            }
        });
    }

    private void launchMainActivity() {
        Intent intent = MainActivity.makeLaunchIntent(WelcomeActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
    }
}