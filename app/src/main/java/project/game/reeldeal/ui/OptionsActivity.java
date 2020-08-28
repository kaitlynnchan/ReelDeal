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
        setupHighScoreText();
        setupGamesStartedText();
        setupResetButtons();
        setupBackButton();
    }

    private void setupRadioButtons() {
        RadioGroup radioGroupFish = findViewById(R.id.radioGroupTotalFishes);
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
                    setupHighScoreText();
                }
            });
            radioGroupFish.addView(radioButtonFish);

            if(buttonNumFishes == numFishes){
                radioButtonFish.setChecked(true);
            }
        }

        RadioGroup radioGroupSize = findViewById(R.id.radioGroupSize);
        int[] numRowArray = this.getResources().getIntArray(R.array.num_rows_array);
        int[] numColumnArray = this.getResources().getIntArray(R.array.num_columns_array);
        for (int i = 0; i < numRowArray.length; i++) {
            final int buttonNumRow = numRowArray[i];
            final int buttonNumColumn = numColumnArray[i];
            RadioButton radioButtonSize = new RadioButton(this);
            radioButtonSize.setText(buttonNumRow + getString(R.string.rows_and) + buttonNumColumn + getString(R.string.columns));
            radioButtonSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numRows = buttonNumRow;
                    numColumns = buttonNumColumn;
                    saveOptions();
                    setupHighScoreText();
                }
            });
            radioGroupSize.addView(radioButtonSize);

            if(buttonNumRow == numRows && buttonNumColumn == numColumns){
                radioButtonSize.setChecked(true);
            }
        }
        setGame();
    }

    private void setGame(){
        Game game = new Game(numRows, numColumns, numFishes, -1);

        // Set high score depending whether config exists or not
        index = configs.getIndex(game);
        if(index == -1){
            highScore = -1;
            configs.add(game);
        } else{
            highScore = configs.get(index).getHighScore();
        }
    }

    private void setupHighScoreText() {
        setGame();

        TextView txtHighScore = findViewById(R.id.textHighScore);
        String strHighScore = getString(R.string.high_score);
        if (highScore == -1) {
            strHighScore += getString(R.string.no_answer);
        } else {
            strHighScore += "" + highScore;
        }
        txtHighScore.setText(strHighScore);
    }

    private void setupGamesStartedText(){
        TextView txtGamesPlayed = findViewById(R.id.textGamesStarted);
        String strGamesPlayed = getString(R.string.games_started);
        strGamesPlayed += "" + configs.getGamesStarted();
        txtGamesPlayed.setText(strGamesPlayed);
    }

    private void setupResetButtons() {
        Button btnResetScore = findViewById(R.id.buttonResetScore);
        btnResetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    highScore = -1;
                    configs.get(index).setHighScore(highScore);
                    MainActivity.saveGameConfigs(OptionsActivity.this, configs);
                    setupHighScoreText();
                }
            }
        });

        Button btnResetGames = findViewById(R.id.buttonResetGamesNum);
        btnResetGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configs.setGamesStarted(0);
                setupGamesStartedText();
                MainActivity.saveGameConfigs(OptionsActivity.this, configs);
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
    }

    public static int getNumFishes(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        int defaultNumFishes = context.getResources().getInteger(R.integer.default_num_fishes);
        return sharedPreferences.getInt(EDITOR_NUM_FISHES, defaultNumFishes);
    }

    public static int getNumRows(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        int defaultNumRows = context.getResources().getInteger(R.integer.default_num_rows);
        return sharedPreferences.getInt(EDITOR_NUM_ROWS, defaultNumRows);
    }

    public static int getNumColumns(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_OPTIONS, MODE_PRIVATE);
        int defaultNumColumns = context.getResources().getInteger(R.integer.default_num_columns);
        return sharedPreferences.getInt(EDITOR_NUM_COLUMNS, defaultNumColumns);
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}