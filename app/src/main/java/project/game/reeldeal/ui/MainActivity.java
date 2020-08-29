package project.game.reeldeal.ui;

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

import project.game.reeldeal.R;
import project.game.reeldeal.model.GameConfigs;
import project.game.reeldeal.model.Game;

/**
 * Main menu
 * Displays buttons to navigate screens
 */
public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_GAMES = "shared_prefs_games";
    private static final String EDITOR_GAME_CONFIGS = "editor_game_configs";
    private static final String EDITOR_GAMES_STARTED = "editor_games_started";
    private static final String EDITOR_IS_GAME_SAVED = "editor_is_game_saved";
    private GameConfigs configs;

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

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configs = GameConfigs.getInstance();
        loadGameConfigs();

        setupBackgroundAnimation();
    }

    private void setupButtons() {
        Button buttonPlay = findViewById(R.id.button_play);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGameConfigs(MainActivity.this, configs, false);
                Intent intent = GameActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button buttonResume = findViewById(R.id.button_resume);
        boolean isGameSaved = getIsGameSaved(this);
        if(isGameSaved){
            buttonResume.setVisibility(View.VISIBLE);
            buttonResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = GameActivity.makeLaunchIntent(MainActivity.this);
                    startActivity(intent);
                }
            });
        } else{
            buttonResume.setVisibility(View.GONE);
        }

        Button buttonOptions = findViewById(R.id.button_options);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        Button buttonHelp = findViewById(R.id.button_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeLaunchIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    private void loadGameConfigs() {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(SHARED_PREFS_GAMES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(EDITOR_GAME_CONFIGS, null);
        Type type = new TypeToken<ArrayList<Game>>() {}.getType();
        ArrayList<Game> arrTemp = gson.fromJson(json, type);
        if(arrTemp != null) {
            configs.setConfigs(arrTemp);
        }

        int gamesStarted = sharedPreferences.getInt(EDITOR_GAMES_STARTED, 0);
        configs.setGamesStarted(gamesStarted);
    }

    public static void saveGameConfigs(Context context, GameConfigs configs, boolean isGameSaved) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_GAMES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(configs.getConfigs());
        editor.putString(EDITOR_GAME_CONFIGS, json);

        editor.putInt(EDITOR_GAMES_STARTED, configs.getGamesStarted());
        editor.putBoolean(EDITOR_IS_GAME_SAVED, isGameSaved);

        editor.apply();
    }

    public static boolean getIsGameSaved(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_GAMES, MODE_PRIVATE);
        return sharedPreferences.getBoolean(EDITOR_IS_GAME_SAVED, false);
    }

    private void createGame() {
        int numFishes = OptionsActivity.getNumFishes(this);
        int rows = OptionsActivity.getNumRows(this);
        int columns = OptionsActivity.getNumColumns(this);
        Game game = new Game(rows, columns, numFishes, -1);

        // Set high score depending whether config exists or not
        int index = configs.getIndex(game);
        if(index == -1){
            configs.add(game);
            index = configs.getIndex(game);
        }
        configs.setCurrentGameIndex(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createGame();
        setupButtons();
    }

    private void setupBackgroundAnimation() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightScreen = displayMetrics.heightPixels;
        final int widthScreen = displayMetrics.widthPixels;

        fishLeft1 = findViewById(R.id.image_fish_left_1);
        fishLeft2 = findViewById(R.id.image_fish_left_2);
        fishRight1 = findViewById(R.id.image_fish_right_2);
        fishRight2 = findViewById(R.id.image_fish_right_1);
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
                        moveFishes(widthScreen);
                    }
                });
            }
        }, 0, 25);

    }

    private void moveFishes(int widthScreen) {
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
