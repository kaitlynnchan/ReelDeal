package cmpt276.assign3.assign3game;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
 * Displays number of scans used, items found, total items, high score, and games played
 * Displays a grid of buttons
 */
public class GameActivity extends AppCompatActivity {
    private ItemsManager items = ItemsManager.getInstance();
    private Button[][] buttons;
    private int scans = 0;
    private int found = 0;
    private int rows = 4;
    private int cols = 6;
    private int totalItems = 2;

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Temporary parameters
        items.setParams(rows,cols, totalItems);

        buttons = new Button[rows][cols];
        items.fillArray();

        setupTextDisplay();
        setupButtonGrid();
    }

    private void setupTextDisplay() {
        String strTotalItems = getString(R.string.items_total);
        strTotalItems += " " + totalItems;

        TextView txtItemTotal = findViewById(R.id.textViewItemsTotal);
        txtItemTotal.setText(strTotalItems);

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
                    1.0f ));
            table.addView(tableRow);

            for(int c = 0; c < cols; c++){
                final int FINAL_ROW = r;
                final int FINAL_COL = c;

                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateButtons(FINAL_ROW, FINAL_COL);
                    }
                });

                tableRow.addView(button);
                buttons[r][c] = button;
            }
        }
    }

    private void updateButtons(int row, int col) {
        Button button = buttons[row][col];
        int count = items.scanRowCol(row, col);
        if(count == -1){
            items.setItemValue(row, col, false);

            // Lock button size
            lockButton();

            // Set and scale image
            int newWidth = button.getWidth();
            int newHeight = button.getHeight();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.confetti);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaledBitmap));

            // Update found count text
            found++;
            TextView txtFound = findViewById(R.id.textViewFoundCount);
            txtFound.setText("" + found);

            // Update already clicked buttons
            updateButtonText(row, col);

            if(found == totalItems){
                // Display win screen
            }

        } else{
            button.setPadding(0,0,0,0);
            button.setText(count + "");

            // Update scan count text
            scans++;
            TextView txtScans = findViewById(R.id.textViewScansCount);
            txtScans.setText("" + scans);

            button.setClickable(false);
        }
    }

    private void lockButton() {
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                Button button = buttons[r][c];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void updateButtonText(int row, int col) {
        for(int r = 0; r < rows; r++){
            Button temp = buttons[r][col];
            setButtonText(temp);
        }
        for(int c = 0; c < cols; c++){
            Button temp = buttons[row][c];
            setButtonText(temp);
        }
    }

    private void setButtonText(Button temp) {
        if(!temp.isClickable()){
            int count = Integer.parseInt(temp.getText().toString());
            if(count > 0){
                count--;
                temp.setText(count + "");
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
