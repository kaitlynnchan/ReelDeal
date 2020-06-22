package cmpt276.assign3.assign3game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cmpt276.assign3.assign3game.model.GameConfigs;
import cmpt276.assign3.assign3game.model.ItemsManager;

/**
 * Game Screen
 * Displays number of scans used, items found, total items, high score, and games played
 * Displays a grid of buttons
 */
public class GameActivity extends AppCompatActivity {

    public static final String TAG_WIN_DIALOG = "Win dialog";
    public static final String SHARED_PREFERENCES = "shared preferences";
    private static final String EXTRA_IS_GAME_SAVED = "is there a game saved";
    public static final String EDITOR_GAMES_PLAYED = "games played";
    public static final String EDITOR_GAME_CONFIG = "game configurations";
    public static final String EDITOR_BUTTON_ARRAY = "button array";
    public static final String EDITOR_IS_GAME_FINISHED = "is the game finished";
    public static final String SHARED_PREFERENCES_BUTTONS = "shared preferences for buttons";
    public static final String EDITOR_BTN_ROWS = "rows";
    public static final String EDITOR_BTN_COLS = "cols";
    public static final String EDITOR_BTN_ITEMS = "item total";

    private ItemsManager items = ItemsManager.getInstance();
    private GameConfigs configs = GameConfigs.getInstance();
    private Button[][] buttons;
    private int rows = items.getRows();
    private int cols = items.getCols();
    private int totalItems = items.getTotalItems();
    private int highScore = items.getHighScore();
    private int gamesPlayed;
    private int scans = 0;
    private int found = 0;
    private int index;
    private boolean isGameFinished = false; // getGameFinished(this)
    private boolean[][] itemRevealed = new boolean[rows][cols];
    private boolean isGameSaved;

    public static Intent makeLaunchIntent(Context context, boolean isGameSaved){
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_IS_GAME_SAVED, isGameSaved);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting parameters
        buttons = new Button[rows][cols];
        index = configs.getIndex(items);

        loadData();
        setupTextDisplay();

        Intent intent = getIntent();
        isGameSaved = intent.getBooleanExtra(EXTRA_IS_GAME_SAVED, false);
        System.out.println("isGameSaved:" + isGameSaved);
        if(isGameSaved){
            loadSavedGame();
        } else{
            items.fillArray();
            setupButtonGrid();
        }
        saveData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        gamesPlayed += sharedPreferences.getInt(EDITOR_GAMES_PLAYED, 1);
        gamesPlayed++;
    }

    private void setupTextDisplay() {
        // Setup total items text
        TextView txtTotalItems = findViewById(R.id.textViewTotalItems);
        String strTotalItems = getString(R.string.total_items);
        strTotalItems += " " + totalItems;
        txtTotalItems.setText(strTotalItems);

        // Setup high score
        TextView txtHighScore = findViewById(R.id.textViewHighScore);
        String strHighScore = getString(R.string.high_score);
        if(highScore == -1){
            strHighScore += "  " + 0;
        } else{
            strHighScore += "  " + highScore;
        }
        txtHighScore.setText(strHighScore);

        // Setup games played
        TextView txtGamesPlayed = findViewById(R.id.textViewGamesPlayed);
        String strGamesPlayed = getString(R.string.games_played);
        strGamesPlayed += "  " + gamesPlayed;
        txtGamesPlayed.setText(strGamesPlayed);
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

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = items.scanRowCol(FINAL_ROW, FINAL_COL);
                        System.out.println(buttons[FINAL_ROW][FINAL_COL].getWidth() + "," + buttons[FINAL_ROW][FINAL_COL].getHeight());
                        updateButtons(FINAL_ROW, FINAL_COL, count);
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtons(int row, int col, int count) {
        Button button = buttons[row][col];
        if(count == -1){
            setItemFound(row, col);

            // Game finished
            if(found == totalItems){
                isGameFinished = true;
                saveData();

                // Setup new high score
                if(highScore == -1 || scans < highScore){
                    configs.get(index).setHighScore(scans);

                    TextView txtHighScore = findViewById(R.id.textViewHighScore);
                    String strHighScore = getString(R.string.high_score);
                    strHighScore += "  " + scans;
                    txtHighScore.setText(strHighScore);
                }

                // Display win screen
                FragmentManager manager = getSupportFragmentManager();
                WinFragment dialogWin = new WinFragment(scans);
                dialogWin.show(manager, TAG_WIN_DIALOG);
            }
        } else{

            // Fix animations to move one at a time
//            buttonAnimate(row, col);

            button.setPadding(0,0,0,0);
            button.setText(count + "");
            button.setClickable(false);

            // Update scan count text
            scans++;
            TextView txtScans = findViewById(R.id.textViewScansCount);
            txtScans.setText("" + scans);
        }
    }

    private void setItemFound(int row, int col) {
        Button button = buttons[row][col];

        // Lets items know that the item has been revealed
        items.setItemValue(row, col, false);
        itemRevealed[row][col] = true;

        // Lock button size
//            lockButton();
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                Button btnLock = buttons[r][c];

                int width = btnLock.getWidth();
                btnLock.setMinWidth(width);
                btnLock.setMaxWidth(width);

                int height = btnLock.getHeight();
                btnLock.setMinHeight(height);
                btnLock.setMaxHeight(height);
            }
        }

        // Set and scale image
//        setButtonImage(row, col);
        int newWidth = button.getWidth();
        int newHeight = button.getHeight();
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.confetti);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));

        // Update found count text
        found++;
        TextView txtFound = findViewById(R.id.textViewFoundCount);
        txtFound.setText("" + found);

        // Update already clicked buttons
//        updateButtonText(row, col);
        for(int r = 0; r < rows; r++){
            Button temp = buttons[r][col];
            setButtonText(temp);
        }
        for(int c = 0; c < cols; c++){
            Button temp = buttons[row][c];
            setButtonText(temp);
        }
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
            Button temp = buttons[rBelow][col];
            temp.startAnimation(waveBelow);
        }

        for(int rAbove = row - 1; rAbove >= 0; rAbove--){
            Button temp = buttons[rAbove][col];
            temp.startAnimation(waveAbove);
        }

        for(int cRight = col + 1; cRight < cols; cRight++){
            Button temp = buttons[row][cRight];
            temp.startAnimation(waveRight);
        }

        for(int cLeft = col - 1; cLeft >= 0; cLeft--){
            Button temp = buttons[row][cLeft];
            temp.startAnimation(waveLeft);
        }
    }

    private void loadSavedGame() {
        SharedPreferences preferencesBtns = getSharedPreferences(SHARED_PREFERENCES_BUTTONS, MODE_PRIVATE);
        if(preferencesBtns.getInt(EDITOR_BTN_ROWS, 0) == rows
                && preferencesBtns.getInt(EDITOR_BTN_COLS, 0) == cols
                && preferencesBtns.getInt(EDITOR_BTN_ITEMS, 0) == totalItems){
            items.setItems(configs.get(index).getArray());

            setupButtonGrid();

            int widthBtn = preferencesBtns.getInt("width", 0);
            int heighthBtn = preferencesBtns.getInt("height", 0);
//
            for(int r = 0; r < rows; r++){
                for(int c = 0; c < cols; c++){
                    boolean isButtonClickable = preferencesBtns.getBoolean("buttons[" + r + "][" + c + "].isClickable", false);
                    if(!isButtonClickable){
                        int count = items.scanRowCol(r, c);
                        updateButtons(r, c, count);
                    }
                    boolean test = preferencesBtns.getBoolean("itemRevealed[" + r + "][" + c + "] value", false);
                    if(test){
                        itemRevealed[r][c] = true;

                        for(int r2 = 0; r2 < rows; r2++){
                            for(int c2 = 0; c2 < cols; c2++){
                                Button btnLock = buttons[r2][c2];

                                btnLock.setMinWidth(widthBtn);
                                btnLock.setMaxWidth(widthBtn);

                                btnLock.setMinHeight(heighthBtn);
                                btnLock.setMaxHeight(heighthBtn);
                            }
                        }

                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.confetti);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, widthBtn, heighthBtn, true);
                        Resources resource = getResources();
                        buttons[r][c].setBackground(new BitmapDrawable(resource, scaledBitmap));

                        found++;
                        TextView txtFound = findViewById(R.id.textViewFoundCount);
                        txtFound.setText("" + found);
                    }
                }

            }
        } else{
            isGameSaved = false;
            items.fillArray();
            setupButtonGrid();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_GAMES_PLAYED, gamesPlayed);
        editor.putBoolean(EDITOR_IS_GAME_FINISHED, isGameFinished);
        System.out.println("save isGameFinished:"+isGameFinished);

        Gson gson = new Gson();
        String json = gson.toJson(configs.getConfigs());
        editor.putString(EDITOR_GAME_CONFIG, json);
        editor.apply();
    }

    private void saveButtonArray(){
        configs.get(index).setItems(items.getArray());
        saveData();

        SharedPreferences preferencesBtns = this.getSharedPreferences(SHARED_PREFERENCES_BUTTONS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesBtns.edit();
        editor.putInt(EDITOR_BTN_ROWS, rows);
        editor.putInt(EDITOR_BTN_COLS, cols);
        editor.putInt(EDITOR_BTN_ITEMS, totalItems);
        editor.putInt("width", buttons[0][0].getWidth());
        editor.putInt("height", buttons[0][0].getHeight());

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                editor.putBoolean("buttons[" + r + "][" + c + "].isClickable", buttons[r][c].isClickable());
                editor.putBoolean("itemRevealed[" + r + "][" + c + "] value", itemRevealed[r][c]);
            }
        }

        editor.apply();
    }

    static public boolean getGameFinished(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        System.out.println("load isGameFinished:"+sharedPreferences.getBoolean(EDITOR_IS_GAME_FINISHED, false));
        return sharedPreferences.getBoolean(EDITOR_IS_GAME_FINISHED, false);
    }

    @Override
    public void onBackPressed() {
        if(!isGameFinished){
            saveButtonArray();
        }
        Intent intent = new Intent();
        setResult(GameActivity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
