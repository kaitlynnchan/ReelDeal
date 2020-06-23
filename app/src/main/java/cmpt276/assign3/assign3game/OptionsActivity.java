package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {

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
        int[] numFishes = getResources().getIntArray(R.array.fishNumber);
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
        int[] row = getResources().getIntArray(R.array.selectedRowSize);
        int[] column = getResources().getIntArray(R.array.selectedColumnSize);
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
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.PREFS), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getString(R.string.EDITOR_FISHES), savedNumOfFishes);
        editor.putInt(getString(R.string.EDITOR_ROWS), savedRows);
        editor.putInt(getString(R.string.EDITOR_COLUMNS), savedColumns);
        editor.apply();

    }

    static public int getNumFishes(Context c){
        SharedPreferences preferences = c.getSharedPreferences(c.getString(R.string.PREFS), MODE_PRIVATE);
        return preferences.getInt(c.getString(R.string.EDITOR_FISHES), 6);
    }

    static public int getNumRows(Context c){
        SharedPreferences preferences = c.getSharedPreferences(c.getString(R.string.PREFS), MODE_PRIVATE);
        return preferences.getInt(c.getString(R.string.EDITOR_ROWS), 4);
    }

    static public int getNumColumns(Context c){
        SharedPreferences preferences = c.getSharedPreferences(c.getString(R.string.PREFS), MODE_PRIVATE);
        return preferences.getInt(c.getString(R.string.EDITOR_COLUMNS), 6);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(OptionsActivity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}