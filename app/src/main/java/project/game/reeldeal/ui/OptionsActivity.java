package project.game.reeldeal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import project.game.reeldeal.R;
import project.game.reeldeal.model.Constant;
import project.game.reeldeal.model.Game;
import project.game.reeldeal.model.GameConfigs;

/**
 * Options Screen
 * Allows user to select options displayed and
 *  to reset saved information
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_OPTIONS = "shared_prefs_options";
    private static final String EDITOR_NUM_FISHES = "editor_num_fishes";
    private static final String EDITOR_NUM_ROWS = "editor_num_rows";
    private static final String EDITOR_NUM_COLUMNS = "editor_num_columns";

    private int numFishes;
    private int numRows;
    private int numColumns;

    private GameConfigs configs = GameConfigs.getInstance();
    private int highScore;
    private int index;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, OptionsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        numFishes = getNumFishes(this);
        numRows = getNumRows(this);
        numColumns = getNumColumns(this);

        setupRadioButtons();
        updateIndex();
        setGamesStartedText();
        setupResetButtons();
        setupBackButton();
    }

    private void setupRadioButtons() {
        RadioGroup radioNumFishes = findViewById(R.id.radio_num_fishes);
        int[] numFishesArray = this.getResources().getIntArray(R.array.num_fishes_array);
        for (int i = 0; i < numFishesArray.length; i++) {
            final int buttonNumFishes = numFishesArray[i];

            RadioButton radioButtonFish = new RadioButton(this);
            radioButtonFish.setText(buttonNumFishes + getString(R.string.fishes));
            radioButtonFish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numFishes = buttonNumFishes;
                    saveOptions();
                    updateIndex();
                }
            });
            radioNumFishes.addView(radioButtonFish);

            if(buttonNumFishes == numFishes){
                radioButtonFish.setChecked(true);
            }
        }

        RadioGroup radioGameSize = findViewById(R.id.radio_game_size);
        int[] numRowArray = this.getResources().getIntArray(R.array.num_rows_array);
        int[] numColumnArray = this.getResources().getIntArray(R.array.num_columns_array);
        for (int i = 0; i < numRowArray.length; i++) {
            final int buttonNumRow = numRowArray[i];
            final int buttonNumColumn = numColumnArray[i];

            RadioButton radioButtonGameSize = new RadioButton(this);
            String strGameSize = buttonNumRow + getString(R.string.rows_and) + buttonNumColumn
                    + getString(R.string.columns);
            radioButtonGameSize.setText(strGameSize);
            radioButtonGameSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numRows = buttonNumRow;
                    numColumns = buttonNumColumn;
                    saveOptions();
                    updateIndex();
                }
            });
            radioGameSize.addView(radioButtonGameSize);

            if(buttonNumRow == numRows && buttonNumColumn == numColumns){
                radioButtonGameSize.setChecked(true);
            }
        }
    }

    private void updateIndex(){
        Game game = new Game(numRows, numColumns, numFishes, -1);

        // Set high score depending whether config exists or not
        index = configs.getIndex(game);
        if(index == -1){
            highScore = -1;
            configs.add(game);
        } else{
            highScore = configs.get(index).getHighScore();
        }
        setHighScoreText();
    }

    private void setHighScoreText() {
        TextView textHighScore = findViewById(R.id.text_high_score);
        String strHighScore = getString(R.string.high_score);
        if (highScore == -1) {
            strHighScore += getString(R.string.no_answer);
        } else {
            strHighScore += "" + highScore;
        }
        textHighScore.setText(strHighScore);
    }

    private void setGamesStartedText(){
        TextView textGamesStarted = findViewById(R.id.text_games_started);
        String strGamesStarted = getString(R.string.games_started);
        strGamesStarted += "" + configs.getGamesStarted();
        textGamesStarted.setText(strGamesStarted);
    }

    private void setupResetButtons() {
        Button buttonResetScore = findViewById(R.id.button_reset_score);
        buttonResetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    highScore = -1;
                    configs.get(index).setHighScore(highScore);
                    MainActivity.saveGameConfigs(OptionsActivity.this, configs,
                            false);
                    updateIndex();
                }
            }
        });

        Button buttonResetGamesStarted = findViewById(R.id.button_reset_games_started);
        buttonResetGamesStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configs.setGamesStarted(0);
                setGamesStartedText();
                MainActivity.saveGameConfigs(OptionsActivity.this, configs,
                        false);
            }
        });
    }

    private void saveOptions() {
        SharedPreferences sharedPreferences =
                this.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_NUM_FISHES, numFishes);
        editor.putInt(EDITOR_NUM_ROWS, numRows);
        editor.putInt(EDITOR_NUM_COLUMNS, numColumns);
        editor.apply();
        MainActivity.saveGameConfigs(this, configs, false);
    }

    public static int getNumFishes(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_NUM_FISHES, Constant.DEFAULT_NUM_FISHES);
    }

    public static int getNumRows(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_NUM_ROWS, Constant.DEFAULT_NUM_ROWS);
    }

    public static int getNumColumns(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_NUM_COLUMNS, Constant.DEFAULT_NUM_COLUMNS);
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}