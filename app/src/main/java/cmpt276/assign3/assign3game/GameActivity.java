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
    private ItemsArray items;
    private int scans = 0;
    private int found = 0;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        items = ItemsArray.getInstance();

        // Temporary setting parameters
        items.setParams(4,6,2);

        items.fillArray();

        setupButtonGrid();
    }

    private void setupButtonGrid() {
        TableLayout table = findViewById(R.id.tableLayoutButtonGrid);

        int rows = items.getRows();
        int cols = items.getCols();
        int itemTotal = items.getItemTotal();

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

                            found++;
                            String strFound = getString(R.string.found_count);
                            strFound = "" + found;

                            TextView txtFound = findViewById(R.id.textViewFoundCount);
                            txtFound.setText(strFound);
                        } else{
                            button.setText(count + "");

                            scans++;
                            String strScans = getString(R.string.scans_used);
                            strScans = "" + scans;

                            TextView txtScans = findViewById(R.id.textViewScansCount);
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
