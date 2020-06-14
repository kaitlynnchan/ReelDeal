package cmpt276.assign3.assign3game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cmpt276.assign3.assign3game.model.ItemsArray;

/**
 * Game Screen
 * Displays grid of buttons
 */
public class GameActivity extends AppCompatActivity {
    private int rows = 4;
    private int cols = 6;
    private int itemTotal = 2;
    private ItemsArray items;
    private int scans = 0;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        items = ItemsArray.getInstance(rows, cols, itemTotal);
        items.fillArray();

        setupButtonGrid();
    }

    private void setupButtonGrid() {
        TableLayout table = findViewById(R.id.tableLayoutButtonGrid);

        for(int r = 0; r < rows; r++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));
            table.addView(tableRow);

            for(int c = 0; c < cols; c++){
                final int FINAL_ROW = r;
                final int FINAL_COL = c;

                final Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = items.scanRowCol(FINAL_ROW, FINAL_COL);
                        if(count == -1){
                            // Set image of button to item
                            // Set array position as false
                        } else{
                            button.setText(count + "");

                            scans++;
                            String strScans = getString(R.string.scans_count);
                            strScans += " " + scans;

                            TextView txtScans = findViewById(R.id.textViewScans);
                            txtScans.setText(strScans);
                        }
                    }
                });

                tableRow.addView(button);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(GameActivity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }
}
