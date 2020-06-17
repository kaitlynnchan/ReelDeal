package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {

    private DisplayMetrics displayMetrics;
    private int heightScreen;
    private int widthScreen;
    private int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

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
        View overlay = findViewById(R.id.viewOverlay);
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.anim_fade_out);
        fadeOut.setFillAfter(true);
        overlay.startAnimation(fadeOut);

        ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
        Animation rotate = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_rotate);
        imgFishingPole.startAnimation(rotate);

        timer += 3000;
        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle, "translationY", (float) (heightScreen / 5) * -1);
        moveY.setStartDelay(timer);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle, "translationX", (float) widthScreen / 5);
        moveX.setStartDelay(timer);
        moveX.setDuration(2000);
        moveX.start();

        timer += 2000 + 500;
        moveAfterFishing();
    }

    private void moveAfterFishing() {
        final TextView gameTitle = findViewById(R.id.textViewGameTitle);;
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

        TextView authors = findViewById(R.id.textViewAuthors);
        ObjectAnimator moveAuthorX = ObjectAnimator.ofFloat(authors,
                "translationX",
                (float) (widthScreen / 3) * 2);
        moveAuthorX.setStartDelay(timer);
        moveAuthorX.setDuration(1000);
        moveAuthorX.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                View overlay = findViewById(R.id.viewOverlay);
                Animation fadeIn = AnimationUtils.loadAnimation(WelcomeScreen.this, R.anim.anim_fade_in);
                fadeIn.setFillAfter(true);
                overlay.startAnimation(fadeIn);

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
