package cmpt276.assign3.assign3game.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cmpt276.assign3.assign3game.R;
import cmpt276.assign3.assign3game.model.GameConfigs;
import cmpt276.assign3.assign3game.model.FishesManager;

/**
 * Main menu
 * Displays: play, options, and help buttons to navigate screens
 */
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_SAVED_GAME = "is there a saved game";
    public static boolean isGameSaved = false;

    private GameConfigs config = GameConfigs.getInstance();
    private int index;
    private int widthScreen;
    private int heightScreen;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private ImageView fishLeft1;
    private ImageView fishLeft2;
    private ImageView fishRight1;
    private ImageView fishRight2;
    private float fishLeft1X;
    private float fishLeft2X;
    private float fishRight1X;
    private float fishRight2X;
    private float fishWidth;

    public static Intent makeLaunchIntent(Context context, boolean isGameSaved){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_SAVED_GAME, isGameSaved);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightScreen = displayMetrics.heightPixels;
        widthScreen = displayMetrics.widthPixels;

        loadData();

        Intent intent = getIntent();
        isGameSaved = intent.getBooleanExtra(EXTRA_SAVED_GAME, false);
        if(isGameSaved){
            createFishesManager();
            Intent intentGame = GameActivity.makeLaunchIntent(this, isGameSaved, index);
            startActivity(intentGame);
        }

        setupButtons();
        setupMainBackground();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(GameActivity.SHARED_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(GameActivity.EDITOR_GAME_CONFIG, null);
        Type type = new TypeToken<ArrayList<FishesManager>>() {}.getType();
        ArrayList<FishesManager> arrTemp = gson.fromJson(json, type);
        if(arrTemp != null) {
            config.setConfigs(arrTemp);
        }
    }

    private void createFishesManager() {
        int numFishes = OptionsActivity.getNumFishes(this);
        int rows = OptionsActivity.getNumRows(this);
        int columns = OptionsActivity.getNumColumns(this);
        FishesManager manager = new FishesManager(rows, columns, numFishes, -1);

        // Set high score depending whether config exists or not
        index = config.getIndex(manager);
        if(index == -1){
            config.add(manager);
            index = config.getIndex(manager);
        }

    }

    private void setupButtons() {
        Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.makeLaunchIntent(MainActivity.this, isGameSaved, index);
                startActivity(intent);
            }
        });


        Button btnOptions = findViewById(R.id.buttonOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button btnHelp = findViewById(R.id.buttonHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        createFishesManager();
        super.onResume();
    }

    private void setupMainBackground() {
        fishLeft1 = findViewById(R.id.imageFishLeft1);
        fishLeft2 = findViewById(R.id.imageFishLeft2);
        fishRight1 = findViewById(R.id.imageFishRight2);
        fishRight2 = findViewById(R.id.imageFishRight1);
        fishWidth = fishLeft1.getWidth();

        fishLeft1.setX(-fishWidth);
        fishLeft1.setY(heightScreen / 2.0f);

        fishLeft2.setX(-fishWidth);
        fishLeft2.setY((heightScreen / 5.0f) * 2);

        fishRight1.setX(widthScreen + fishWidth);
        fishRight1.setY(heightScreen / 3.0f);

        fishRight2.setX(widthScreen + fishWidth);
        fishRight2.setY((heightScreen / 3.0f) * 2);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        moveFishes();
                    }
                });
            }
        }, 0, 25);

    }

    private void moveFishes() {
        // Move fish left
        fishLeft1X += 5;
        if(fishLeft1X > widthScreen + fishWidth){
            fishLeft1X = -200f;
        }
        fishLeft1.setX(fishLeft1X);

        fishLeft2X += 2;
        if(fishLeft2X > widthScreen + fishWidth){
            fishLeft2X = -200f;
        }
        fishLeft2.setX(fishLeft2X);

        // Move fish right
        fishRight1X -= 4f;
        if(fishRight1X < -200f){
            fishRight1X = widthScreen + 200f;
        }
        fishRight1.setX(fishRight1X);

        fishRight2X -= 3f;
        if(fishRight2X < -200f){
            fishRight2X = widthScreen + 200f;
        }
        fishRight2.setX(fishRight2X);
    }
}
