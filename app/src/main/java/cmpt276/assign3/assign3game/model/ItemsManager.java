package cmpt276.assign3.assign3game.model;

import android.content.Intent;

import static android.content.Intent.getIntent;

/**
 * ItemsManager class
 * Stores a 2D array of booleans
 *
 * Value of boolean legend:
 *      true = item present
 *      false = item not present
 */
public class ItemsManager {
    private boolean[][] items;
    private int rows;
    private int cols;
    private int totalItems;

    // Singleton implementation of ItemsManager
    private static ItemsManager instance;
    private ItemsManager(){/*to prevent others from instantiating*/}
    public static ItemsManager getInstance(){
        if(instance == null){
            instance = new ItemsManager();
        }
        return instance;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setItemValue(int row, int col, boolean value) {
        items[row][col] = value;
    }

    public void fillArray(){
        items = new boolean[rows][cols];
        // Randomly add itemTotal amount of items into array
        int tempTotalItems = totalItems;
        while(tempTotalItems > 0){
            int row = (int) ( Math.random() * rows );
            int col = (int) ( Math.random() * cols );
            if(!isItemThere(row, col)) {
                items[row][col] = true;
                tempTotalItems--;
            }
        }
    }

    public int scanRowCol(int row, int col){
        if(isItemThere(row, col)){
            return -1;
        }

        int count = 0;
        for(int r = 0; r < rows; r++){
            if(isItemThere(r, col)){
                count++;
            }
        }
        for(int c = 0; c < cols; c++){
            if(isItemThere(row, c)){
                count++;
            }
        }
        return count;
    }

    public boolean isItemThere(int row, int col){
        return items[row][col];
    }
/*
    Intent intent =  getIntent();

    private Intent getIntent() {
        return intent;
    }

    String choice = intent.getStringExtra("KEY");*/
}
