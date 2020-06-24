package cmpt276.assign3.assign3game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    int index;

    public static Intent makeLaunchIntent(Context context, boolean isGameSaved){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_SAVED_GAME, isGameSaved);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // Implement background based on theme
    }
}
