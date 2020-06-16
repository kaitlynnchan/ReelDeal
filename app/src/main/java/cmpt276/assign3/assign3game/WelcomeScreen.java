package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {

    private int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        moveFishingRod();
        setupSkipButton();

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent i = MainActivity.makeLaunchIntent(WelcomeScreen.this); startActivity(i);
                finish();
            }
        }, timer*2);
    }

    private void moveFishingRod() {
        ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
        Animation rotate = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_rotate);
        imgFishingPole.startAnimation(rotate);

        timer += 3000;
        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle, "translationY", -250);
        moveY.setStartDelay(timer);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle, "translationX", 350);
        moveX.setStartDelay(timer);
        moveX.setDuration(2000);
        moveX.start();

        timer += 2000 + 500;
        moveAfterFishing();
    }

    private void moveAfterFishing() {
        final TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY2 = ObjectAnimator.ofFloat(gameTitle, "translationY", -350);
        moveY2.setStartDelay(timer);
        moveY2.setDuration(1000);
        moveY2.start();

        ObjectAnimator moveX2 = ObjectAnimator.ofFloat(gameTitle, "translationX", 225);
        moveX2.setStartDelay(timer);
        moveX2.setDuration(1000);
        moveX2.start();

        TextView authors = findViewById(R.id.textViewAuthors);
        ObjectAnimator moveAuthorX = ObjectAnimator.ofFloat(authors, "translationX", 1100);
        moveAuthorX.setStartDelay(timer);
        moveAuthorX.setDuration(1000);
        moveAuthorX.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                View overlay = findViewById(R.id.viewOverlay);
                overlay.setAlpha((float) 0.5);

                Animation scale = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_zoom);
                scale.setFillAfter(true);
                gameTitle.startAnimation(scale);

                timer += 1000 + 4000;
            }
        }, timer);
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
