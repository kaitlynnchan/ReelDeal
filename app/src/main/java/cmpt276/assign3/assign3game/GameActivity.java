package cmpt276.assign3.assign3game;

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
    public static final String EDITOR_GAMES_PLAYED = "games played";
    public static final String EDITOR_GAME_CONFIG = "game configurations";
    public static final String EDITOR_IS_GAME_FINISHED = "is the game finished";
    public static final String SHARED_PREFERENCES_BUTTONS = "shared preferences for buttons";
    public static final String EDITOR_WIDTH = "button width";
    public static final String EDITOR_HEIGHT = "button height";

    private static final String EXTRA_IS_GAME_SAVED = "is there a game saved";

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
    private boolean[][] itemRevealed = new boolean[rows][cols];

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
        boolean isGameSaved = intent.getBooleanExtra(EXTRA_IS_GAME_SAVED, false);
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
                        updateButtons(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtons(int row, int col) {
        int count = items.scanRowCol(row, col);
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
            buttonAnimate(row, col);

            setScan(row, col, count);
        }
    }

    private void setItemFound(int row, int col) {
        Button button = buttons[row][col];

        // Lets items know that the item has been revealed
        items.setItemValue(row, col, false);
        itemRevealed[row][col] = true;

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
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.confetti);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
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

    private void setScan(int row, int col, int count) {
        Button button = buttons[row][col];
        button.setPadding(0,0,0,0);
        button.setText(count + "");
        button.setClickable(false);

        // Update scan count text
        scans++;
        TextView txtScans = findViewById(R.id.textViewScansCount);
        txtScans.setText("" + scans);
    }

    private void loadSavedGame() {
        SharedPreferences preferencesBtns = getSharedPreferences(SHARED_PREFERENCES_BUTTONS, MODE_PRIVATE);
        items.setItems(configs.get(index).getArray());
        setupButtonGrid();

        int widthBtn = preferencesBtns.getInt(EDITOR_WIDTH, 0);
        int heightBtn = preferencesBtns.getInt(EDITOR_HEIGHT, 0);

        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                boolean isButtonClickable = preferencesBtns.getBoolean("buttons[" + r + "][" + c + "].isClickable", false);
                if(!isButtonClickable){
                    int count = items.scanRowCol(r, c);
                    setScan(r, c, count);
                }

                boolean test = preferencesBtns.getBoolean("itemRevealed[" + r + "][" + c + "] value", false);
                if(test){
                    itemRevealed[r][c] = true;
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
        editor.putInt(EDITOR_GAMES_PLAYED, gamesPlayed);
        editor.putBoolean(EDITOR_IS_GAME_FINISHED, isGameFinished);

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
        editor.putInt(EDITOR_WIDTH, buttons[0][0].getWidth());
        editor.putInt(EDITOR_HEIGHT, buttons[0][0].getHeight());

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
