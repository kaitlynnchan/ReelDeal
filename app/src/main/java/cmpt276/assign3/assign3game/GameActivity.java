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

import cmpt276.assign3.assign3game.model.ItemsManager;

/**
 * Game Screen
 * Displays grid of buttons
 */
public class GameActivity extends AppCompatActivity {
    private ItemsManager items;
    private Button[][] buttons;
    private int scans = 0;
    private int found = 0;
    private int rows;
    private int cols;
    private int itemTotal;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        items = ItemsManager.getInstance();

        // Temporary parameters since options have not been created yet
        items.setParams(4,6,2);
        rows = items.getRows();
        cols = items.getCols();
        itemTotal = items.getItemTotal();

        buttons = new Button[rows][cols];

        items.fillArray();

        setupText();
        setupButtonGrid();
    }

    private void setupText() {
        String strItemTotal = getString(R.string.items_total);
        strItemTotal += " " + itemTotal;

        TextView txtItemTotal = findViewById(R.id.textViewItemsTotal);
        txtItemTotal.setText(strItemTotal);

        // Setup high score

        // Setup games played
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

                            items.setItemValue(FINAL_ROW, FINAL_COL, false);

                            // Update found count text
                            found++;
                            String strFound = getString(R.string.found_count);
                            strFound = "" + found;

                            TextView txtFound = findViewById(R.id.textViewFoundCount);
                            txtFound.setText(strFound);

                            // Update already clicked buttons
                            updateButtonText(FINAL_ROW, FINAL_COL);

                            if(found == itemTotal){
                                // Display win screen
                            }

                        } else{
                            button.setPadding(0,0,0,0);
                            button.setText(count + "");

                            // Update scan count text
                            scans++;
                            String strScans = getString(R.string.scans_used);
                            strScans = "" + scans;

                            TextView txtScans = findViewById(R.id.textViewScansCount);
                            txtScans.setText(strScans);
                            button.setClickable(false);

                        }
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtonText(int row, int col) {
        for(int r = 0; r < rows; r++){
            Button temp = buttons[r][col];
            if(!temp.isClickable()){
                int count = Integer.parseInt(temp.getText().toString());
                if(count > 0){
                    count--;
                    temp.setText(count + "");
                }
            }
        }
        for(int c = 0; c < cols; c++){
            Button temp = buttons[row][c];
            if(!temp.isClickable()){
                int count = Integer.parseInt(temp.getText().toString());
                if(count > 0){
                    count--;
                    temp.setText(count + "");
                }
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
