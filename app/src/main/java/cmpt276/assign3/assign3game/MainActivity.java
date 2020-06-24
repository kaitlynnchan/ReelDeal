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
    public static final int REQUEST_CODE_GAME = 42;
    public static final int REQUEST_CODE_OPTIONS = 43;
    public static final int REQUEST_CODE_HELP = 44;
    public static boolean isGameSaved = false;

    private GameConfigs config = GameConfigs.getInstance();

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
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

        createFishesManager();
    }

    private void createFishesManager() {
        FishesManager manager = FishesManager.getInstance();

        int numFishes = OptionsActivity.getNumFishes(this);
        manager.setTotalFishes(numFishes);
        int rows = OptionsActivity.getNumRows(this);
        manager.setRows(rows);
        int columns = OptionsActivity.getNumColumns(this);
        manager.setCols(columns);

        // Set high score depending whether config exists or not
        int index = config.getIndex(manager);
        if(index == -1){
            manager.setHighScore(-1);
            config.add(manager);
        } else{
            int highScore = config.get(index).getHighScore();
            manager.setHighScore(highScore);
        }

        boolean isGameFinished = GameActivity.getGameFinished(this);
        if(!isGameFinished){
            isGameSaved = true;
        }
    }

    private void setupButtons() {
        final Button btnPlay = findViewById(R.id.buttonPlay);
        btnPlay.setBackground(this.getResources().getDrawable(R.drawable.button_shadow));
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));

                Intent intent = GameActivity.makeLaunchIntent(MainActivity.this, isGameSaved);
                startActivityForResult(intent, REQUEST_CODE_GAME);
            }
        });


        final Button btnOptions = findViewById(R.id.buttonOptions);
        btnOptions.setBackground(this.getResources().getDrawable(R.drawable.button_shadow));
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOptions.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));

                Intent intent = OptionsActivity.makeLaunchIntent(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_OPTIONS);
            }
        });

        final Button btnHelp = findViewById(R.id.buttonHelp);
        btnHelp.setBackground(this.getResources().getDrawable(R.drawable.button_shadow));
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHelp.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.button_border));

                Intent intent = HelpActivity.makeLaunchIntent(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_HELP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setupButtons();

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        switch (requestCode){
            case REQUEST_CODE_GAME:
                createFishesManager();
                break;
            case REQUEST_CODE_OPTIONS:
//                createFishesManager();
                isGameSaved = false;
                break;
            default:
                assert false;

        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        createFishesManager();
//        isGameSaved = false;
//    }

    private void setupMainBackground() {
        // Implement background based on theme
    }
}
