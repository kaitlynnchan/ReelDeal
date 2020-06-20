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

    private RadioGroup radioGroupObject;
    private int savedNumObjects;
    private RadioGroup radioGroupSize;
    private int savedRows;
    private int savedColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        savedNumObjects = getNumObjects(this);
        radioButtons();
    }

    private void radioButtons() {
        radioGroupObject = findViewById(R.id.radioGroupObjectTotal);
//        textViewObject = findViewById(R.id.settingsText);
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
                    saveValues();
                }
            });
            radioGroupObject.addView(radioButtonObject);

            if(numObj == savedNumObjects){
                radioButtonObject.setChecked(true);
            }
        }

         radioGroupSize = findViewById(R.id.radioGroupSize);
//        textViewSize = findViewById(R.id.text_Size);
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

                    saveValues();
                }
            });
            radioGroupSize.addView(radioButtonSize);
            if(numR == savedRows && numC == savedColumns){
                radioButtonSize.setChecked(true);
            }
        }

    }

    private void saveValues() {
        SharedPreferences preferences = this.getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Objects", savedNumObjects);
        editor.putInt("Rows", savedRows);
        editor.putInt("Columns", savedColumns);
        editor.apply();

    }

    static public int getNumObjects(Context c){
        SharedPreferences preferences = c.getSharedPreferences("Prefs", MODE_PRIVATE);
        // Need to define a default value
        return preferences.getInt("Objects", 6);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(OptionsActivity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }
}