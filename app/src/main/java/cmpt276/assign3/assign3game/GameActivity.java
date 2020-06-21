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
    private int rows = items.getRows();
    private int cols = items.getCols();
    private int totalItems = items.getTotalItems();

    public static Intent makeLaunchIntent(Context context){
        Intent intent = new Intent(context, GameActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

                final Button button = new Button(this);
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
            for(int r = 0; r < rows; r++){
                for(int c = 0; c < cols; c++){
                    Button btn = buttons[r][c];

                    int width = btn.getWidth();
                    btn.setMinWidth(width);
                    btn.setMaxWidth(width);

                    int height = btn.getHeight();
                    btn.setMinHeight(height);
                    btn.setMaxHeight(height);
                }
            }

            // Set and scale image
            button.setBackgroundResource(R.drawable.ic_launcher_background);
//            int newWidth = button.getWidth();
//            int newHeight = button.getHeight();
//            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_android_black_24dp);
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
//            Resources resource = getResources();
//            button.setBackground(new BitmapDrawable(resource, scaledBitmap));

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
