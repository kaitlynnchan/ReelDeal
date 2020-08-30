package project.game.reeldeal.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewTreeObserver;
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
import project.game.reeldeal.model.SoundEffect;

/**
 * Game Screen
 * Display and allows user to play game and see date
 */
public class GameActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_RESUME = 50;
    public static final int REQUEST_CODE_STOP = 51;

    private static final String TAG_WIN_DIALOG = "tag_win_dialog";
    private static final String TAG_PAUSE_DIALOG = "tag_pause_dialog";

    private GameConfigs configs;
    private Game game;
    private int rows;
    private int cols;
    private int totalFishes;
    private int highScore;
    private int scans = 0;
    private int found = 0;
    private Button[][] buttons;
    private SoundPool soundPool;

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
        cols = game.getColumns();
        totalFishes = game.getTotalFishes();
        highScore = game.getHighScore();

        soundPool = SoundEffect.buildSoundPool();
        SoundEffect.loadSounds(this, soundPool);

        buttons = new Button[rows][cols];
        setupButtonGrid();

        boolean isGameSaved = MainActivity.getIsGameSaved(this);
        if(isGameSaved){
            TableLayout table = findViewById(R.id.table_button_grid);
            ViewTreeObserver treeObserver = table.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    loadSavedGameState();
                }
            });
        } else{
            configs.incrementGamesStarted();
            game.fillArray();
        }

        setupDisplayText();
        setupPauseButton();
        setupBackButton();
    }

    private void setupButtonGrid() {
        TableLayout table = findViewById(R.id.table_button_grid);
        table.removeAllViews();

        for(int row = 0; row < rows; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f ));
            table.addView(tableRow);

            for(int col = 0; col < cols; col++){
                final int FINAL_ROW = row;
                final int FINAL_COL = col;

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
                buttons[row][col] = button;
            }
        }
    }

    private void loadSavedGameState() {
        game.setGameBoard(configs.getCurrentGame().getGameBoard());
        scans = 0;
        found = 0;

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                boolean isButtonClickable = game.getTile(row, col).isClickable();
                if(!isButtonClickable){
                    int count = game.scanRowCol(row, col);
                    setScanButtonText(row, col, count);
                }

                Button button = buttons[row][col];
                int widthBtn = button.getWidth();
                int heightBtn = button.getHeight();

                boolean isFishRevealed = game.getTile(row, col).isFishRevealed();
                boolean isFishThere = game.getTile(row, col).isFishThere();
                if(isFishThere && isFishRevealed){
                    lockButtons(widthBtn, heightBtn);
                    setButtonImage(button, widthBtn, heightBtn);
                    updateFoundCountTxt();
                }
            }
        }
    }

    private void updateButtons(int row, int col) {
        // Adding vibration to buttons
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        int count = game.scanRowCol(row, col);
        if(count == -1){
            fishFoundMedia.start();
            setFishesFound(row, col);
            SoundEffect.playSound(soundPool, SoundEffect.FOUND);
            vibrator.vibrate(4000);

            setFishButton(row, col);
            checkGameFinished();
        } else{
            // Fix animations to move one at a time
            buttonAnimate(row, col);
            SoundEffect.playSound(soundPool, SoundEffect.SCAN);
            vibrator.vibrate(2500);

            animateScanning(row, col);
            setScanButtonText(row, col, count);
        }
    }

    private void setFishButton(int row, int col) {
        game.getTile(row, col).setFishRevealed(true);

        Button button = buttons[row][col];
        int width = button.getWidth();
        int height = button.getHeight();
        lockButtons(width, height);
        setButtonImage(button, width, height);
        updateFoundCountTxt();

        // Update already clicked buttons in row and col
        for(int r = 0; r < rows; r++){
            Button temp = buttons[r][col];
            updateButtonText(temp);
        }
        for(int c = 0; c < cols; c++){
            Button temp = buttons[row][c];
            updateButtonText(temp);
        }
    }

    private void lockButtons(int width, int height) {
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                Button buttonLock = buttons[row][col];

                buttonLock.setMinWidth(width);
                buttonLock.setMaxWidth(width);

                buttonLock.setMinHeight(height);
                buttonLock.setMaxHeight(height);
            }
        }
    }

    private void setButtonImage(Button button, int newWidth, int newHeight) {
        Resources resources = this.getResources();
        Bitmap originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.fish);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth,
                newHeight, true);
        button.setBackground(new BitmapDrawable(resources, scaledBitmap));
    }

    private void updateFoundCountTxt() {
        found++;
        TextView textFound = findViewById(R.id.text_found_count);
        textFound.setText("" + found);
    }

    private void updateButtonText(Button button) {
        if(!button.isClickable()){
            int count = Integer.parseInt(button.getText().toString());
            if(count > 0){
                count--;
                button.setText(count + "");
            }
        }
    }

    private void checkGameFinished() {
        if(found == totalFishes){
            // Setup new high score
            if(highScore == -1 || scans < highScore){
                configs.getCurrentGame().setHighScore(scans);

                TextView textHighScore = findViewById(R.id.text_high_score);
                String strHighScore = getString(R.string.high_score);
                strHighScore += "  " + scans;
                textHighScore.setText(strHighScore);
            }

            saveGameState(false);

            FragmentManager fragmentManager = getSupportFragmentManager();
            WinDialog dialogWin = new WinDialog(scans, highScore);
            dialogWin.show(fragmentManager, TAG_WIN_DIALOG);
        }
    }

    private void animateScanning(int row, int col) {
        Animation waveLeft = new TranslateAnimation(0, -15, 0, 0);
        Animation waveRight = new TranslateAnimation(0, 15, 0, 0);
        Animation waveAbove = new TranslateAnimation(0, 0, 0, -15);
        Animation waveBelow = new TranslateAnimation(0, 0, 0, 15);

        animateWave(waveLeft);
        animateWave(waveRight);
        animateWave(waveAbove);
        animateWave(waveBelow);

        for(int rBelow = row + 1; rBelow < rows; rBelow++){
            startButtonAnimation(rBelow, col, waveBelow);
        }

        for(int rAbove = row - 1; rAbove >= 0; rAbove--){
            startButtonAnimation(rAbove, col, waveAbove);
        }

        for(int cRight = col + 1; cRight < cols; cRight++){
            startButtonAnimation(row, cRight, waveRight);
        }

        for(int cLeft = col - 1; cLeft >= 0; cLeft--){
            startButtonAnimation(row, cLeft, waveLeft);
        }
    }

    private void animateWave(Animation wave) {
        wave.setDuration(500);
        wave.setRepeatCount(1);
        wave.setRepeatMode(Animation.REVERSE);
    }

    private void startButtonAnimation(int row, int col, Animation animation) {
        Button button = buttons[row][col];
        if (!game.getTile(row, col).isFishRevealed() && game.getTile(row, col).isClickable()) {
            button.startAnimation(animation);
        }
    }

    private void setScanButtonText(int row, int col, int count) {
        Button button = buttons[row][col];
        button.setPadding(0,0,0,0);
        button.setText(count + "");
        button.setTextSize(16);
        button.setClickable(false);
        game.getTile(row, col).setClicked();

        // Update scan count text
        scans++;
        TextView textScans = findViewById(R.id.text_scans_count);
        textScans.setText("" + scans);
    }

    private void setupDisplayText() {
        // Setup total Fishes text
        TextView textTotalFishes = findViewById(R.id.text_total_fishes);
        String strTotalFishes = getString(R.string.total_fishes);
        strTotalFishes += "" + totalFishes;
        textTotalFishes.setText(strTotalFishes);

        // Setup high score
        TextView textHighScore = findViewById(R.id.text_high_score);
        String strHighScore = getString(R.string.high_score);
        if(highScore == -1){
            strHighScore += getString(R.string.no_answer);
        } else{
            strHighScore += "" + highScore;
        }
        textHighScore.setText(strHighScore);

        // Setup games started
        TextView textGamesStarted = findViewById(R.id.text_games_started);
        String strGamesStarted = getString(R.string.games_started);
        strGamesStarted += "" + configs.getGamesStarted();
        textGamesStarted.setText(strGamesStarted);
    }

    private void saveGameState(boolean isGameSaved){
        configs.getCurrentGame().setGameBoard(game.getGameBoard());
        MainActivity.saveGameConfigs(this, configs, isGameSaved);
    }

    private void setupPauseButton() {
        Button buttonPause = findViewById(R.id.button_pause);
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
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                buttons[row][col].setBackgroundResource(R.drawable.button_corner);
                buttons[row][col].setText(null);
            }
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if(found != totalFishes){
            saveGameState(true);
        }
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
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
        if(found != totalFishes){
            saveGameState(true);
        }
    }
}