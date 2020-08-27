package cmpt276.assign3.assign3game.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import cmpt276.assign3.assign3game.R;
import cmpt276.assign3.assign3game.model.GameConfigs;
import cmpt276.assign3.assign3game.model.FishesManager;

/**
 * Game Screen
 * Displays:
 *      grid of buttons,
 *      number of scans used,
 *      fishes found,
 *      total fishes,
 *      high score,
 *      and games played
 * Allows users to play the game according to game logic
 */
public class GameActivity extends AppCompatActivity {

    public static final String TAG_WIN_DIALOG = "Win dialog";
    public static final String SHARED_PREFERENCES = "shared preferences";
    public static final String EDITOR_GAMES_STARTED = "games started";
    public static final String EDITOR_GAME_CONFIG = "game configurations";
    public static final String EDITOR_IS_GAME_FINISHED = "is the game finished";
    public static final String SHARED_PREFERENCES_BUTTONS = "shared preferences for buttons";
    public static final String EDITOR_WIDTH = "button width";
    public static final String EDITOR_HEIGHT = "button height";

    private static final String EXTRA_IS_GAME_SAVED = "is there a game saved";
    public static final String EXTRA_CONFIGURATION_INDEX = "configuration index";

    private GameConfigs configs = GameConfigs.getInstance();
    private FishesManager manager;
    private int rows;
    private int cols;
    private int totalFishes;
    private int highScore;
    private int gamesStarted;
    private int scans = 0;
    private int found = 0;
    private int index;
    private boolean isGameFinished = false;
    private Button[][] buttons;
    private boolean[][] fishRevealed ;
    private Vibrator vibrator;

    public static Intent makeLaunchIntent(Context context, boolean isGameSaved, int index){
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_IS_GAME_SAVED, isGameSaved);
        intent.putExtra(EXTRA_CONFIGURATION_INDEX, index);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gamesStarted = getGamesStarted(this);

        Intent intent = getIntent();
        index = intent.getIntExtra(EXTRA_CONFIGURATION_INDEX, -1);
        manager = configs.get(index);
        rows = manager.getRows();
        cols = manager.getCols();
        totalFishes = manager.getTotalFishes();
        highScore = manager.getHighScore();
        buttons = new Button[rows][cols];
        fishRevealed = new boolean[rows][cols];


        boolean isGameSaved = intent.getBooleanExtra(EXTRA_IS_GAME_SAVED, false);
        if(isGameSaved){
            loadSavedGame();
        } else{
            gamesStarted++;
            manager.fillArray();
            setupButtonGrid();
        }

        setupTextDisplay();
        saveData();
        setupBackButton();
        setupPauseButton();
    }

    private void setupTextDisplay() {
        // Setup total Fishes text
        TextView txtTotalFishes = findViewById(R.id.textViewTotalFishes);
        String strTotalFishes = getString(R.string.total_fishes);
        strTotalFishes += "" + totalFishes;
        txtTotalFishes.setText(strTotalFishes);

        // Setup high score
        TextView txtHighScore = findViewById(R.id.textViewHighScore);
        String strHighScore = getString(R.string.high_score);
        if(highScore == -1){
            strHighScore += getString(R.string.no_answer);
        } else{
            strHighScore += "" + highScore;
        }
        txtHighScore.setText(strHighScore);

        // Setup games started
        TextView txtGamesStarted = findViewById(R.id.textViewGamesStarted);
        String strGamesStarted = getString(R.string.games_started);
        strGamesStarted += "" + gamesStarted;
        txtGamesStarted.setText(strGamesStarted);
    }

    private void setupButtonGrid() {
        TableLayout table = findViewById(R.id.tableLayoutButtonGrid);

        for(int r = 0; r < rows; r++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f ));
            table.addView(tableRow);

            for(int c = 0; c < cols; c++){
                final int FINAL_ROW = r;
                final int FINAL_COL = c;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.setBackgroundResource(R.drawable.button_corner);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateButtons(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtons(int row, int col) {
        // Adding vibration to buttons
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        //Adding sounds to button click in game
        final MediaPlayer media = MediaPlayer.create(this, R.raw.sonar_low);
        final MediaPlayer fishFoundMedia = MediaPlayer.create(this, R.raw.sonar_high);

        int count = manager.scanRowCol(row, col);
        if(count == -1){
            setFishesFound(row, col);
            fishFoundMedia.start();
            vibrator.vibrate(4000);
            // Game finished
            if(found == totalFishes){
                // Setup new high score
                if(highScore == -1 || scans < highScore){
                    configs.get(index).setHighScore(scans);

                    TextView txtHighScore = findViewById(R.id.textViewHighScore);
                    String strHighScore = getString(R.string.high_score);
                    strHighScore += "  " + scans;
                    txtHighScore.setText(strHighScore);
                }

                isGameFinished = true;
                MainActivity.isGameSaved = false;
                saveData();

                // Display win screen
                FragmentManager fragmentManager = getSupportFragmentManager();
                WinFragment dialogWin = new WinFragment(scans, highScore);
                dialogWin.show(fragmentManager, TAG_WIN_DIALOG);
            }
        } else{
            // Fix animations to move one at a time
            buttonAnimate(row, col);
            media.start();
            vibrator.vibrate(2500);
            setScan(row, col, count);
        }
    }

    private void setFishesFound(int row, int col) {
        Button button = buttons[row][col];

        // Lets manager know that the fish has been revealed
        manager.setArrayIndexValue(row, col, false);
        fishRevealed[row][col] = true;

        int width = button.getWidth();
        int height = button.getHeight();
        lockButton(width, height);
        setButtonImage(row, col, width, height);
        updateFoundCountTxt();

        // Update already clicked buttons
        for(int r = 0; r < rows; r++){
            Button temp = buttons[r][col];
            setButtonText(temp);
        }
        for(int c = 0; c < cols; c++){
            Button temp = buttons[row][c];
            setButtonText(temp);
        }
    }

    private void lockButton(int width, int height) {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                Button btnLock = buttons[r][c];

                btnLock.setMinWidth(width);
                btnLock.setMaxWidth(width);

                btnLock.setMinHeight(height);
                btnLock.setMaxHeight(height);
            }
        }
    }

    private void setButtonImage(int row, int col, int newWidth, int newHeight) {
        Button button = buttons[row][col];
        Resources resources = this.getResources();
        Bitmap originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.fish);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        button.setBackground(new BitmapDrawable(resources, scaledBitmap));
    }

    private void updateFoundCountTxt() {
        found++;
        TextView txtFound = findViewById(R.id.textViewFoundCount);
        txtFound.setText("" + found);
    }

    private void setButtonText(Button temp) {
        if(!temp.isClickable()){
            int count = Integer.parseInt(temp.getText().toString());
            if(count > 0){
                count--;
                temp.setText(count + "");
            }
        }
    }

    private void buttonAnimate(int row, int col) {
        Animation waveLeft = new TranslateAnimation(0, -15, 0, 0);
        waveLeft.setDuration(500);
        waveLeft.setRepeatCount(1);
        waveLeft.setRepeatMode(Animation.REVERSE);

        Animation waveRight = new TranslateAnimation(0, 15, 0, 0);
        waveRight.setDuration(500);
        waveRight.setRepeatCount(1);
        waveRight.setRepeatMode(Animation.REVERSE);

        Animation waveAbove = new TranslateAnimation(0, 0, 0, -15);
        waveAbove.setDuration(500);
        waveAbove.setRepeatCount(1);
        waveAbove.setRepeatMode(Animation.REVERSE);

        Animation waveBelow = new TranslateAnimation(0, 0, 0, 15);
        waveBelow.setDuration(500);
        waveBelow.setRepeatCount(1);
        waveBelow.setRepeatMode(Animation.REVERSE);

        for(int rBelow = row + 1; rBelow < rows; rBelow++){
            Button btnBelow = buttons[rBelow][col];
            if(!fishRevealed[rBelow][col] && btnBelow.isClickable()){
                btnBelow.startAnimation(waveBelow);
            }
        }

        for(int rAbove = row - 1; rAbove >= 0; rAbove--){
            Button btnAbove = buttons[rAbove][col];
            if(!fishRevealed[rAbove][col] && btnAbove.isClickable()){
                btnAbove.startAnimation(waveAbove);
            }
        }

        for(int cRight = col + 1; cRight < cols; cRight++){
            Button btnRight = buttons[row][cRight];
            if(!fishRevealed[row][cRight] && btnRight.isClickable()){
                btnRight.startAnimation(waveRight);
            }
        }

        for(int cLeft = col - 1; cLeft >= 0; cLeft--){
            Button btnLeft = buttons[row][cLeft];
            if(!fishRevealed[row][cLeft] && btnLeft.isClickable()){
                btnLeft.startAnimation(waveLeft);
            }
        }
    }

    private void setScan(int row, int col, int count) {
        Button button = buttons[row][col];
        button.setPadding(0,0,0,0);
        button.setText(count + "");
        button.setTextSize(16);
        button.setClickable(false);

        // Update scan count text
        scans++;
        TextView txtScans = findViewById(R.id.textViewScansCount);
        txtScans.setText("" + scans);
    }

    private void loadSavedGame() {
        SharedPreferences preferencesBtns = this.getSharedPreferences(SHARED_PREFERENCES_BUTTONS, MODE_PRIVATE);
        manager.setArray(configs.get(index).getArray());
        setupButtonGrid();

        int widthBtn = preferencesBtns.getInt(EDITOR_WIDTH, 0);
        int heightBtn = preferencesBtns.getInt(EDITOR_HEIGHT, 0);

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                boolean isButtonClickable = preferencesBtns.getBoolean("buttons[" + r + "][" + c + "].isClickable", false);
                if(!isButtonClickable){
                    int count = manager.scanRowCol(r, c);
                    setScan(r, c, count);
                }

                boolean test = preferencesBtns.getBoolean("fishRevealed[" + r + "][" + c + "] value", false);
                if(test){
                    fishRevealed[r][c] = true;
                    lockButton(widthBtn, heightBtn);
                    setButtonImage(r, c, widthBtn, heightBtn);
                    updateFoundCountTxt();
                }
            }
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_GAMES_STARTED, gamesStarted);
        editor.putBoolean(EDITOR_IS_GAME_FINISHED, isGameFinished);

        Gson gson = new Gson();
        String json = gson.toJson(configs.getConfigs());
        editor.putString(EDITOR_GAME_CONFIG, json);
        editor.apply();
    }

    private void saveGameState(){
        configs.get(index).setArray(manager.getArray());
        saveData();

        SharedPreferences preferencesBtns = this.getSharedPreferences(SHARED_PREFERENCES_BUTTONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesBtns.edit();
        editor.putInt(EDITOR_WIDTH, buttons[0][0].getWidth());
        editor.putInt(EDITOR_HEIGHT, buttons[0][0].getHeight());

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                editor.putBoolean("buttons[" + r + "][" + c + "].isClickable", buttons[r][c].isClickable());
                editor.putBoolean("fishRevealed[" + r + "][" + c + "] value", fishRevealed[r][c]);
            }
        }

        editor.apply();
    }

    static public boolean getGameFinished(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getBoolean(EDITOR_IS_GAME_FINISHED, true);
    }

    static public int getGamesStarted(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getInt(EDITOR_GAMES_STARTED, 0);
    }

    @Override
    protected void onUserLeaveHint() {
        if(!isGameFinished){
            saveGameState();
        }
        super.onUserLeaveHint();
    }

    private void setupPauseButton() {
        Button buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PauseActivity.makeLaunchIntent(GameActivity.this);
                startActivityForResult(intent, 50);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_CANCELED){
            return;
        }

        if(requestCode == 50){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        isGameFinished = true;
        MainActivity.isGameSaved = false;
        saveData();
        finish();
        super.onBackPressed();
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGameFinished = true;
                MainActivity.isGameSaved = false;
                saveData();
                finish();
            }
        });
    }
}