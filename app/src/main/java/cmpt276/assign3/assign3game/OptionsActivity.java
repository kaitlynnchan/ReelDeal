package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cmpt276.assign3.assign3game.model.ItemsManager;

public class OptionsActivity extends AppCompatActivity {

    RadioGroup radioGroupObject;
    RadioButton radioButtonObject;
    TextView textViewObject;
    RadioGroup radioGroupSize;
    RadioButton radioButtonSize;
    TextView textViewSize;
    private ItemsManager itemsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        radioButtons();
        int savedNumObj = getNumOfObjects();
    }

    private void radioButtons() {
        radioGroupObject = findViewById(R.id.radioGroupSettings);
        textViewObject = findViewById(R.id.settingsText);
        /*int[] numObj = getResources().getIntArray(R.array.objectNumber);
        for (int i = 0; i < numObj.length; i++)
        {
            int num = numObj[i];
            radioButtonObject.setText(num + " Objects");
            radioButtonObject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, "Selected " + radioButtonObject.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            radioGroupObject.addView(radioButtonObject);
        }*/
       radioGroupSize = findViewById(R.id.radioGroupSize);
        textViewSize = findViewById(R.id.text_Size);
        /*int[] row = getResources().getIntArray(R.array.objectSizeRow);
        int[] column = getResources().getIntArray(R.array.objectSizeColumn);
        for (int i = 0; i < row.length; i++)
        {
            int numR = row[i];
            int numC = column[i];
            radioButtonSize.setText(numR + " rows & " + numC + " columns");
            radioButtonSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            radioGroupSize.addView(radioButtonSize);
        }*/
    }

    public void checkButton(View v)
    {
        int radioIDObject = radioGroupObject.getCheckedRadioButtonId();
        radioButtonObject = findViewById(radioIDObject);
        Toast.makeText(OptionsActivity.this, "Selected " + radioButtonSize.getText(), Toast.LENGTH_SHORT).show();
        int radioIDSize = radioGroupSize.getCheckedRadioButtonId();
        radioButtonSize = findViewById(radioIDSize);
        Toast.makeText(OptionsActivity.this, "Selected " + radioButtonSize.getText(), Toast.LENGTH_SHORT).show();
        // need to get choice with switch case
        int choiceObject=0, choiceRow = 0, choiceColumn = 0;
        if (radioIDObject == R.id.object6) {
            choiceObject = 6;
        } else if (radioIDObject == R.id.object10) {
            choiceObject = 10;
        } else if (radioIDObject == R.id.object15) {
            choiceObject = 15;
        } else if (radioIDObject == R.id.object20) {
            choiceObject = 20;
        }

        // checking size of row and column from radio group
        if (radioIDSize == R.id.size4X6) {
            choiceRow = 4;
            choiceColumn = 6;
        } else if (radioIDSize == R.id.size5X10) {
            choiceRow = 5;
            choiceColumn = 10;
        } else if (radioIDSize == R.id.size6X15) {
            choiceRow = 6;
            choiceColumn = 15;
        }
        saveValues(choiceObject, choiceRow, choiceColumn);
        itemsManager.setParams(choiceRow, choiceColumn, choiceObject);
    }

    private void saveValues(int choice, int rows, int columns) {
        SharedPreferences preferences = this.getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Objects", choice);
        editor.putInt("Rows", rows);
        editor.putInt("Columns", columns);
        editor.apply();
    }
    public int getNumOfObjects()
    {
        itemsManager = ItemsManager.getInstance();
        SharedPreferences preferences = this.getSharedPreferences("Prefs", MODE_PRIVATE);
        //Change default value
        return preferences.getInt("Objects", 0);
    }
}