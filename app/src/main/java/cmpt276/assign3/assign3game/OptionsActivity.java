package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Options Screen
 * Displays: radio buttons for number of fishes and size of game board
 */
public class OptionsActivity extends AppCompatActivity {

    public static final String PREFS = "prefs";
    public static final String EDITOR_FISHES = "number of fishes";
    public static final String EDITOR_ROWS = "Rows";
    public static final String EDITOR_COLUMNS = "Columns";
    public int savedNumOfFishes;
    public int savedRows;
    public int savedColumns;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, OptionsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        savedNumOfFishes = getNumFishes(this);
        savedRows = getNumRows(this);
        savedColumns = getNumColumns(this);

        radioButtons();
    }

    private void radioButtons() {
        RadioGroup radioGroupFish = findViewById(R.id.radioGroupTotalFishes);
        int[] numFishes = this.getResources().getIntArray(R.array.fish_number);
        for (int i = 0; i < numFishes.length; i++)
        {
            final int numFish = numFishes[i];
            RadioButton radioButtonFish = new RadioButton(this);
            radioButtonFish.setText(numFish + getString(R.string.fishes));
            radioButtonFish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedNumOfFishes = numFish;
                    savePreferences();
                }
            });
            radioGroupFish.addView(radioButtonFish);

            if(numFish == savedNumOfFishes){
                radioButtonFish.setChecked(true);
            }
        }

        RadioGroup radioGroupSize = findViewById(R.id.radioGroupSize);
        int[] row = this.getResources().getIntArray(R.array.selected_row_size);
        int[] column = this.getResources().getIntArray(R.array.selected_column_size);
        for (int i = 0; i < row.length; i++)
        {
            final int numRow = row[i];
            final int numColumn = column[i];
            RadioButton radioButtonSize = new RadioButton(this);
            radioButtonSize.setText(numRow + getString(R.string.rows_and) + numColumn + getString(R.string.columns));
            radioButtonSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedRows = numRow;
                    savedColumns = numColumn;
                    savePreferences();
                }
            });
            radioGroupSize.addView(radioButtonSize);

            if(numRow == savedRows && numColumn == savedColumns){
                radioButtonSize.setChecked(true);
            }
        }

    }

    private void savePreferences() {
        SharedPreferences preferences = this.getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(EDITOR_FISHES, savedNumOfFishes);
        editor.putInt(EDITOR_ROWS, savedRows);
        editor.putInt(EDITOR_COLUMNS, savedColumns);
        editor.apply();
    }

    static public int getNumFishes(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        int defaultNumFishes = c.getResources().getInteger(R.integer.default_fish_num);
        return preferences.getInt(EDITOR_FISHES, defaultNumFishes);
    }

    static public int getNumRows(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        int defaultRows = c.getResources().getInteger(R.integer.default_row_size);
        return preferences.getInt(EDITOR_ROWS, defaultRows);
    }

    static public int getNumColumns(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        int defaultCols = c.getResources().getInteger(R.integer.default_column_size);
        return preferences.getInt(EDITOR_COLUMNS, defaultCols);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(OptionsActivity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}