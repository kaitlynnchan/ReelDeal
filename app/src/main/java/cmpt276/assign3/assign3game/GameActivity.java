package cmpt276.assign3.assign3game;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public static final String EXTRA_IS_GAME_SAVED = "is there a game saved";
    public static final String EDITOR_GAMES_PLAYED = "games played";
    public static final String EDITOR_GAME_CONFIG = "game configurations";
    public static final String EDITOR_BUTTON_ARRAY = "button array";
    public static final String EDITOR_IS_GAME_FINISHED = "is the game finished";

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
    private boolean isGameFinished = false;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting parameters
        buttons = new Button[rows][cols];
        index = configs.getIndex(items);
        items.fillArray();

        loadData();
        gamesPlayed++;
        saveData();

        setupTextDisplay();
        setupButtonGrid();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        gamesPlayed += sharedPreferences.getInt(EDITOR_GAMES_PLAYED, 1);
        isGameFinished = sharedPreferences.getBoolean(EDITOR_IS_GAME_FINISHED, true);
//        if(!isGameFinished){
//            Gson gson = new Gson();
//            String jsonBtn = sharedPreferences.getString(EDITOR_BUTTON_ARRAY, null);
//            Type type = new TypeToken<Button[][]>() {}.getType();
//            Button[][] arrTemp = gson.fromJson(jsonBtn, type);
//            if(arrTemp != null) {
//                buttons = arrTemp;
//            }
//
//            items = configs.get(index);
//            rows = items.getRows();
//            cols = items.getCols();
//            totalItems = items.getTotalItems();
//            highScore = items.getHighScore();
//
//            if(buttons.length == rows && buttons[0].length == cols){
//                setSavedGame();
//            }
//        }
    }


//    private void setSavedGame() {
//        for(int r = 0; r < buttons.length; r++){
//            for(int c = 0; c < buttons[r].length; c++){
//                if(!buttons[r][c].isClickable()){
//                    updateButtons(r, c);
//                    System.out.println("button");
//                }
//            }
//        }
//
//    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(EDITOR_GAMES_PLAYED, gamesPlayed);
        editor.putBoolean(EDITOR_IS_GAME_FINISHED, isGameFinished);

        Gson gson = new Gson();
        String json = gson.toJson(configs.getConfigs());
        editor.putString(EDITOR_GAME_CONFIG, json);

        String jsonBtn = gson.toJson(buttons);
        editor.putString(EDITOR_BUTTON_ARRAY, jsonBtn);
        editor.apply();
    }

    private void setupTextDisplay() {
        String strTotalItems = getString(R.string.items_total);
        strTotalItems += " " + totalItems;

        TextView txtItemTotal = findViewById(R.id.textViewItemsTotal);
        txtItemTotal.setText(strTotalItems);

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
                        updateButtons(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtons(int row, int col) {
        Button button = buttons[row][col];
        int count = items.scanRowCol(row, col);
        if(count == -1){
            items.setItemValue(row, col, false);

            // Lock button size
            lockButton();

            // Set and scale image
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
            updateButtonText(row, col);

            if(found == totalItems){
                // Display win screen
                FragmentManager manager = getSupportFragmentManager();
                WinFragment dialogWin = new WinFragment(scans);
                dialogWin.show(manager, TAG_WIN_DIALOG);

//                isGameFinished = true;
//
//                // Setup new high score
//                if(highScore == -1 || scans < highScore){
//                    configs.get(index).setHighScore(scans);
//                    saveData();
//
//                    TextView txtHighScore = findViewById(R.id.textViewHighScore);
//                    String strHighScore = getString(R.string.high_score);
//                    strHighScore += "  " + scans;
//                    txtHighScore.setText(strHighScore);
//                }
            }

        } else{

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

            button.setPadding(0,0,0,0);
            button.setText(count + "");

            // Update scan count text
            scans++;
            TextView txtScans = findViewById(R.id.textViewScansCount);
            txtScans.setText("" + scans);

            button.setClickable(false);
        }
    }

    private void lockButton() {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                Button button = buttons[r][c];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void updateButtonText(int row, int col) {
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

    @Override
    public void onBackPressed() {
//        if(!isGameFinished){
//            saveData();
//        }
        Intent intent = new Intent();
        setResult(GameActivity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
