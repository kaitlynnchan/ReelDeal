package cmpt276.assign3.assign3game;

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

/**
 * Welcome Screen
 * Plays a fishing animation
 * Allows users to be able to skip the welcome
 * animation and go to main menu
 */
public class WelcomeScreen extends AppCompatActivity {

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

        fishingGameTitle();
        setupSkipButton();

        handler.postDelayed(new Runnable() {
            @Override public void run() {
                Intent intent = MainActivity.makeLaunchIntent(WelcomeScreen.this);
                startActivity(intent);
                finish();
            }
        }, timer);
    }

    private void fishingGameTitle() {
        // Fishing pole animations
        ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
        Animation rotate = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_rotate);
        imgFishingPole.startAnimation(rotate);

        timer += 3000;

        // Game title animations
        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle,
                "translationY",
                (float) (heightScreen / 5) * -1);
        moveY.setStartDelay(timer);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle,
                "translationX",
                (float) widthScreen / 5);
        moveX.setStartDelay(timer);
        moveX.setDuration(2000);
        moveX.start();

        timer += 2000 + 500;
        moveAfterFishing();
    }

    private void moveAfterFishing() {
        // Black overlay animation
        View overlay = findViewById(R.id.viewOverlay);
        overlay.setVisibility(View.VISIBLE);
        ViewPropertyAnimator fadeIn = overlay.animate();
        fadeIn.alpha(0.5f);
        fadeIn.setStartDelay(timer);
        fadeIn.setDuration(1000);

        // Game title animations
        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle,
                "translationY",
                (float) (heightScreen / 3) * -1);
        moveY.setStartDelay(timer);
        moveY.setDuration(1000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle,
                "translationX",
                (float) widthScreen / 7);
        moveX.setStartDelay(timer);
        moveX.setDuration(1000);
        moveX.start();

        Animation scale = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_zoom);
        scale.setStartOffset(timer);
        scale.setFillAfter(true);
        gameTitle.startAnimation(scale);

        // Author animation
        TextView authors = findViewById(R.id.textViewAuthors);
        ObjectAnimator moveAuthorX = ObjectAnimator.ofFloat(authors,
                "translationX",
                (float) (widthScreen / 3) * 2);
        moveAuthorX.setStartDelay(timer);
        moveAuthorX.setDuration(1000);
        moveAuthorX.start();

        timer += 1000 + 4000;
    }

    private void setupSkipButton() {
        final Button btnSkip = findViewById(R.id.buttonSkip);
        btnSkip.setBackground(this.getResources().getDrawable(R.drawable.button_shadow));
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSkip.setBackground(WelcomeScreen.this.getResources().getDrawable(R.drawable.button_border));
                handler.removeCallbacksAndMessages(null);

                Intent intent = MainActivity.makeLaunchIntent(WelcomeScreen.this);
                startActivity(intent);
                finish();
            }
        });
    }
}
