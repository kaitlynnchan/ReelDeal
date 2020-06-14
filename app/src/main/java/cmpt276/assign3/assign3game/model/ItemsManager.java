package cmpt276.assign3.assign3game.model;

/**
 * ItemsManager class
 * Stores a collection of booleans
 *
 * Value of boolean legend:
 *      true = item present
 *      false = item not present
 */
public class ItemsManager {
    private boolean[][] items;
    private int rows;
    private int cols;
    private int itemTotal;

    // Singleton implementation of ItemsManager
    private static ItemsManager instance;
    private ItemsManager(){}
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

    public int getItemTotal() {
        return itemTotal;
    }

    public void setParams(int rows, int cols, int itemTotal){
        this.rows = rows;
        this.cols = cols;
        this.itemTotal = itemTotal;
    }

    public void setItemValue(int row, int col, boolean value) {
        items[row][col] = value;
    }

    public void fillArray(){
        items = new boolean[rows][cols];

        // Set every value to default false
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                items[r][c] = false;
            }
        }

        // Randomly add itemTotal amount of items into array
        int i = itemTotal;
        while(i > 0){
            int row = (int) ( Math.random() * rows );
            int col = (int) ( Math.random() * cols );
            if(!isThere(row, col)) {
                items[row][col] = true;
                i--;
            }
        }
    }

    public int scanRowCol(int row, int col){
        if(isThere(row, col)){
            return -1;
        }

        int count = 0;
        for(int r = 0; r < rows; r++){
            if(isThere(r, col)){
                count++;
            }
        }
        for(int c = 0; c < cols; c++){
            if(isThere(row, c)){
                count++;
            }
        }
        return count;
    }

    public boolean isThere(int row, int col){
        return items[row][col];
    }

}
