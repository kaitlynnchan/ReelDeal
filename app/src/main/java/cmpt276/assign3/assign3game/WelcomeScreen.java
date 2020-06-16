package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
        }, 9000);
    }

    private void startTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                View overlay = findViewById(R.id.viewOverlay);
                overlay.setAlpha((float)0.5);

                ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
                TextView gameTitle = findViewById(R.id.textViewGameTitle);
                imgFishingPole.setAlpha(0);
                gameTitle.setAlpha(0);

                TextView gameTitleCenter = findViewById(R.id.textViewTitleCenter);
                TextView authors = findViewById(R.id.textViewAuthors);
                gameTitleCenter.setAlpha(1);
                authors.setAlpha(1);
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
