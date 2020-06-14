package cmpt276.assign3.assign3game;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

/**
 * Main menu
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playWelcomeScreen();
        setupButtons();
        setupMainBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void playWelcomeScreen() {
        // Implement welcome screen
    }

    private void setupButtons() {
        final Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup game activity
                btnPlay.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));
            }
        });


        final Button btnOptions = findViewById(R.id.buttonOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup options activity
                btnOptions.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));
            }
        });

        final Button btnHelp = findViewById(R.id.buttonHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setup to help activity
                btnHelp.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));
            }
        });
    }

    private void setupMainBackground() {
        // Implement background based on theme
    }
}
