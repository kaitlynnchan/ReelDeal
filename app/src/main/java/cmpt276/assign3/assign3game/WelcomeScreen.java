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

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        moveFishingRod();

        setupSkipButton();
//        new Handler().postDelayed(new Runnable() {
//            @Override public void run() {
//                Intent i = MainActivity.makeLaunchIntent(WelcomeScreen.this); startActivity(i);
//                finish();
//            }
//        }, 3000);
    }


    private void moveFishingRod() {
//        rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        ImageView imgFishingPole = findViewById(R.id.imageFishingPole);
        Animation rotate = new RotateAnimation(0f, 30f, 50, 50);
        rotate.setDuration(2500);
        rotate.setRepeatCount(1);
        rotate.setRepeatMode(Animation.REVERSE);
//        rotate.setFillAfter(true);
        imgFishingPole.setAnimation(rotate);
        imgFishingPole.setVisibility(View.INVISIBLE);

        TextView gameTitle = findViewById(R.id.textViewGameTitle);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(gameTitle, "translationY", -250);
        moveY.setStartDelay(3000);
        moveY.setDuration(2000);
        moveY.start();

        ObjectAnimator moveX = ObjectAnimator.ofFloat(gameTitle, "translationX", 350);
        moveX.setStartDelay(3000);
        moveX.setDuration(2000);
        moveX.start();



//        if(imgFishingPole.getVisibility() == View.INVISIBLE){
//            ImageView imgFishingRod = findViewById(R.id.imageFishingRod);
//            ImageView imgFishingWire = findViewById(R.id.imageFishingWire);
//            imgFishingRod.setVisibility(View.VISIBLE);
//            imgFishingWire.setVisibility(View.VISIBLE);
//
//        }
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
