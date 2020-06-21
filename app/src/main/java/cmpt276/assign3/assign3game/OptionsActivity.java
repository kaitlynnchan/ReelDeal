package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cmpt276.assign3.assign3game.model.ItemsManager;

public class OptionsActivity extends AppCompatActivity {

    public static final String PREFS = "Prefs";
    public static final String EDITOR_OBJECTS = "Objects";
    public static final String EDITOR_ROWS = "Rows";
    public static final String EDITOR_COLUMNS = "Columns";
    public int savedNumObjects;
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
        savedNumObjects = getNumObjects(this);
        savedRows = getNumRows(this);
        savedColumns = getNumColumns(this);
        radioButtons();

    }

    private void radioButtons() {
        RadioGroup radioGroupObject = findViewById(R.id.radioGroupObjectTotal);
        int[] numObjs = getResources().getIntArray(R.array.objectNumber);
        for (int i = 0; i < numObjs.length; i++)
        {
            final int numObj = numObjs[i];
            RadioButton radioButtonObject = new RadioButton(this);
            radioButtonObject.setText(numObj + " Objects");
            radioButtonObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedNumObjects = numObj;
                    savePreferences();
                }
            });
            radioGroupObject.addView(radioButtonObject);

            if(numObj == savedNumObjects){
                radioButtonObject.setChecked(true);
            }
        }

        RadioGroup radioGroupSize = findViewById(R.id.radioGroupSize);
        int[] row = getResources().getIntArray(R.array.objectSizeRow);
        int[] column = getResources().getIntArray(R.array.objectSizeColumn);
        for (int i = 0; i < row.length; i++)
        {
            final int numR = row[i];
            final int numC = column[i];
            RadioButton radioButtonSize = new RadioButton(this);
            radioButtonSize.setText(numR + " rows & " + numC + " columns");
            radioButtonSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedRows = numR;
                    savedColumns = numC;
                    savePreferences();
                }
            });
            radioGroupSize.addView(radioButtonSize);
            if(numR == savedRows && numC == savedColumns){
                radioButtonSize.setChecked(true);
            }
        }

    }

    private void savePreferences() {
        SharedPreferences preferences = this.getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(EDITOR_OBJECTS, savedNumObjects);
        editor.putInt(EDITOR_ROWS, savedRows);
        editor.putInt(EDITOR_COLUMNS, savedColumns);
        editor.apply();

    }

    static public int getNumObjects(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        return preferences.getInt(EDITOR_OBJECTS, 2);
    }

    static public int getNumRows(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        return preferences.getInt(EDITOR_ROWS, 4);
    }

    static public int getNumColumns(Context c){
        SharedPreferences preferences = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        return preferences.getInt(EDITOR_COLUMNS, 6);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(OptionsActivity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}