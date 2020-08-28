package project.game.reeldeal.ui;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import project.game.reeldeal.R;
import project.game.reeldeal.model.GameConfigs;
import project.game.reeldeal.model.Game;

/**
 * Game Screen
 * Display and allows user to play game and see date
 */
public class GameActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_RESUME = 50;
    public static final int REQUEST_CODE_STOP = 51;

    private static final String TAG_WIN_DIALOG = "tag_win_dialog";
    private static final String TAG_PAUSE_DIALOG = "tag_pause_dialog";
    private static final String SHARED_PREFS_GAME_STATE = "shared_prefs_game_state";
    private static final String EDITOR_IS_GAME_SAVED = "editor_is_game_saved";
    private static final String EDITOR_BUTTON_SIZE = "editor_button_size";

    private GameConfigs configs;
    private Game game;
    private int rows;
    private int cols;
    private int totalFishes;
    private int highScore;
    private int scans = 0;
    private int found = 0;
    private Button[][] buttons;

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, GameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        configs = GameConfigs.getInstance();
        game = configs.getCurrentGame();
        rows = game.getRows();
        cols = game.getCols();
        totalFishes = game.getTotalFishes();
        highScore = game.getHighScore();

        buttons = new Button[rows][cols];

        boolean isGameSaved = getIsGameSaved(this);
        if(isGameSaved){
            loadSavedGameState();
        } else{
            configs.incrementGamesStarted();
            game.fillArray();
            setupButtonGrid();
        }

        setupTextDisplay();
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
        strGamesStarted += "" + configs.getGamesStarted();
        txtGamesStarted.setText(strGamesStarted);
    }

    private void loadSavedGameState() {
        game.setGameBoard(configs.getCurrentGame().getGameBoard());
        setupButtonGrid();

        SharedPreferences sharedPreferences =
                this.getSharedPreferences(SHARED_PREFS_GAME_STATE, MODE_PRIVATE);

        String buttonSize = sharedPreferences.getString(EDITOR_BUTTON_SIZE, "0,0");
        assert buttonSize != null;
        String[] split = buttonSize.split(",");
        int widthBtn = Integer.parseInt(split[0]);
        int heightBtn = Integer.parseInt(split[1]);

        scans = 0;
        found = 0;

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                boolean isButtonClickable = game.getTile(row, col).isClickable();
                if(!isButtonClickable){
                    int count = game.scanRowCol(row, col);
                    setScan(row, col, count);
                }

                boolean isFishRevealed = game.getTile(row, col).isFishRevealed();
                boolean isFishThere = game.getTile(row, col).isFishThere();
                if(isFishThere && isFishRevealed){
                    lockButton(widthBtn, heightBtn);
                    setButtonImage(row, col, widthBtn, heightBtn);
                    updateFoundCountTxt();
                }
            }
        }
    }

    private void setupButtonGrid() {
        TableLayout table = findViewById(R.id.tableLayoutButtonGrid);
        table.removeAllViews();

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
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //Adding sounds to button click in game
        final MediaPlayer media = MediaPlayer.create(this, R.raw.sonar_low);
        final MediaPlayer fishFoundMedia = MediaPlayer.create(this, R.raw.sonar_high);

        int count = game.scanRowCol(row, col);
        if(count == -1){
            setFishesFound(row, col);
            fishFoundMedia.start();
            vibrator.vibrate(4000);
            // Game finished
            if(found == totalFishes){
                // Setup new high score
                if(highScore == -1 || scans < highScore){
                    configs.getCurrentGame().setHighScore(scans);

                    TextView txtHighScore = findViewById(R.id.textViewHighScore);
                    String strHighScore = getString(R.string.high_score);
                    strHighScore += "  " + scans;
                    txtHighScore.setText(strHighScore);
                }

                // Display win screen
                FragmentManager fragmentManager = getSupportFragmentManager();
                WinDialog dialogWin = new WinDialog(scans, highScore);
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

        // Lets game know that the fish has been revealed
        game.getTile(row, col).setFishRevealed(true);

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
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth,
                newHeight, true);
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
        Animation waveRight = new TranslateAnimation(0, 15, 0, 0);
        Animation waveAbove = new TranslateAnimation(0, 0, 0, -15);
        Animation waveBelow = new TranslateAnimation(0, 0, 0, 15);

        animateWave(waveLeft);
        animateWave(waveRight);
        animateWave(waveAbove);
        animateWave(waveBelow);

        for(int rBelow = row + 1; rBelow < rows; rBelow++){
            animateButtonWave(col, rBelow, waveBelow);
        }

        for(int rAbove = row - 1; rAbove >= 0; rAbove--){
            animateButtonWave(col, rAbove, waveAbove);
        }

        for(int cRight = col + 1; cRight < cols; cRight++){
            animateButtonWave(cRight, row, waveRight);
        }

        for(int cLeft = col - 1; cLeft >= 0; cLeft--){
            animateButtonWave(cLeft, row, waveLeft);
        }
    }

    private void animateButtonWave(int col, int row, Animation wave) {
        Button button = buttons[row][col];
        if (!game.getTile(row, col).isFishRevealed() && game.getTile(row, col).isClickable()) {
            button.startAnimation(wave);
        }
    }

    private void animateWave(Animation wave) {
        wave.setDuration(500);
        wave.setRepeatCount(1);
        wave.setRepeatMode(Animation.REVERSE);
    }

    private void setScan(int row, int col, int count) {
        Button button = buttons[row][col];
        button.setPadding(0,0,0,0);
        button.setText(count + "");
        button.setTextSize(16);
        button.setClickable(false);
        game.getTile(row, col).setClicked();

        // Update scan count text
        scans++;
        TextView txtScans = findViewById(R.id.textViewScansCount);
        txtScans.setText("" + scans);
    }

    private void saveGameState(boolean isGameSaved){
        configs.getCurrentGame().setGameBoard(game.getGameBoard());

        SharedPreferences sharedPreferences =
                this.getSharedPreferences(SHARED_PREFS_GAME_STATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(EDITOR_IS_GAME_SAVED, isGameSaved);

        String buttonSize = buttons[0][0].getWidth() + "," + buttons[0][0].getHeight();
        editor.putString(EDITOR_BUTTON_SIZE, buttonSize);

        editor.apply();
        MainActivity.saveGameConfigs(this, configs);
    }

    public static boolean getIsGameSaved(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_GAME_STATE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(EDITOR_IS_GAME_SAVED, false);
    }

    private void setupPauseButton() {
        Button buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserLeaveHint();

                FragmentManager fragmentManager = getSupportFragmentManager();
                PauseDialog dialogPause = new PauseDialog();
                dialogPause.interfaceCommunicator = new PauseDialog.InterfaceCommunicator() {
                    @Override
                    public void sendRequestCode(int code) {
                        if(code == REQUEST_CODE_STOP){
                            onBackPressed();
                        } else if(code == REQUEST_CODE_RESUME){
                            loadSavedGameState();
                        }
                    }
                };
                createBlankGame();
                dialogPause.show(fragmentManager, TAG_PAUSE_DIALOG);
            }
        });
    }

    private void createBlankGame(){
        for(int r = 0; r < buttons.length; r++){
            for(int c = 0; c < buttons[r].length; c++){
                buttons[r][c].setBackgroundResource(R.drawable.button_corner);
                buttons[r][c].setText(null);
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        saveGameState(true);
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveGameState(false);
    }
}