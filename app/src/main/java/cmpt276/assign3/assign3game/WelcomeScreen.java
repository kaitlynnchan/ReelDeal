package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        moveFishingRod();
        setupSkipButton();
        startTimer();

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent i = MainActivity.makeLaunchIntent(WelcomeScreen.this); startActivity(i);
                finish();
            }
        }, 10000);
    }

    private void startTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                View overlay = findViewById(R.id.viewOverlay);
                overlay.setAlpha((float)0.5);

                ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
                imgFishingPole.setAlpha(0);

                TextView gameTitle = findViewById(R.id.textViewGameTitle);
                Animation scale = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_zoom);
                scale.setFillAfter(true);
                gameTitle.startAnimation(scale);

            }
        }, 5500);
    }


    private void moveFishingRod() {
        ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
        Animation rotate = new RotateAnimation(0f, 30f, 50, 50);
        rotate.setDuration(2500);
        rotate.setRepeatCount(1);
        rotate.setRepeatMode(Animation.REVERSE);
        imgFishingPole.setAnimation(rotate);

        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle, "translationY", -250);
        moveY.setStartDelay(3000);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle, "translationX", 350);
        moveX.setStartDelay(3000);
        moveX.setDuration(2000);
        moveX.start();

        // Move
        ObjectAnimator moveY2 = ObjectAnimator.ofFloat(gameTitle, "translationY", -350);
        moveY2.setStartDelay(5500);
        moveY2.setDuration(1000);
        moveY2.start();

        ObjectAnimator moveX2 = ObjectAnimator.ofFloat(gameTitle, "translationX", 225);
        moveX2.setStartDelay(5500);
        moveX2.setDuration(1000);
        moveX2.start();

        TextView authors = findViewById(R.id.textViewAuthors);
        ObjectAnimator moveAuthorX = ObjectAnimator.ofFloat(authors, "translationX", 1100);
        moveAuthorX.setStartDelay(5525);
        moveAuthorX.setDuration(1000);
        moveAuthorX.start();
    }

    private void setupSkipButton() {
        final Button btnSkip = findViewById(R.id.buttonSkip);
        btnSkip.setBackground(this.getResources().getDrawable(R.drawable.button_shadow));
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSkip.setBackground(WelcomeScreen.this.getResources().getDrawable(R.drawable.button_border));

                Intent intent = MainActivity.makeLaunchIntent(WelcomeScreen.this);
                startActivity(intent);
                finish();
            }
        });
    }
}
