package cmpt276.assign3.assign3game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        radioButtons();

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
                    Toast.makeText(OptionsActivity.this, "Selected " + radioButtonSize.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            radioGroupSize.addView(radioButtonSize);
        }*/
    }

    public void checkButton(View v)
    {
        int radioIDObject = radioGroupObject.getCheckedRadioButtonId();
        radioButtonObject = findViewById(radioIDObject);
        ;
        int radioIDSize = radioGroupSize.getCheckedRadioButtonId();
        radioButtonSize = findViewById(radioIDSize);
        Toast.makeText(this, "Selected " + radioButtonSize.getText(), Toast.LENGTH_SHORT).show();
        // Intent i = new Intent(OptionsActivity.this, ItemsManager.class);
        // i.putExtra("KEY", radioButtonObject.getText());
        // startActivity(i);
    }
}