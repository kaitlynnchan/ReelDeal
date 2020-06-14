package cmpt276.assign3.assign3game.model;

public class ItemsArray {
    private boolean[][] items;
    private int rows;
    private int cols;
    private int itemTotal;

    // Singleton implementation of ItemsArray
    private static ItemsArray instance;
    private ItemsArray(){}
    public static ItemsArray getInstance(){
        if(instance == null){
            instance = new ItemsArray();
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
        this.items = new boolean[rows][cols];
    }

    public void setItemValue(int row, int col, boolean value) {
        items[row][col] = value;
    }

    public void fillArray(){
        // Set every value to default false
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                items[r][c] = false;
                System.out.println("should work");
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
