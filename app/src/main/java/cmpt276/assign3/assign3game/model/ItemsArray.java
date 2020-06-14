package cmpt276.assign3.assign3game.model;

public class ItemsArray {
    private boolean[][] items;
    private int rows;
    private int cols;
    private int itemTotal;

    // Singleton implementation of ItemsArray
    private static ItemsArray instance;
    private ItemsArray(int rows, int cols, int itemTotal){
        this.items = new boolean[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.itemTotal = itemTotal;
    }
    public static ItemsArray getInstance(int rows, int cols, int itemTotal){
        if(instance == null){
            instance = new ItemsArray(rows, cols, itemTotal);
        }
        return instance;
    }

    public void fillArray(){
        // Randomly add itemTotal amount of items into array
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                items[r][c] = false;
            }
        }
    }

    public int scanRowCol(int row, int col){
        if(items[row][col] == true){
            return -1;
        }

        int count = 0;
        for(int r = row; r < rows; r++){
            if(items[r][col] == true){
                count++;
            }
        }
        for(int c = col; c < cols; c++){
            if(items[row][c] == true){
                count++;
            }
        }
        return count;
    }

    public boolean isThere(int row, int col){
        return items[row][col];
    }
}
