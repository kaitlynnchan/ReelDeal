package cmpt276.assign3.assign3game.model;

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
//    private int[][] highScoreConfig;
//    private final int COL_ROWS = 0;
//    private final int COL_COLS = 1;
//    private final int COL_TOTAL_ITEMS = 2;
//    private final int COL_HIGH_SCORE = 3;

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

    public int getTotalItems() {
        return totalItems;
    }

//    public int[][] getHighScoreConfig(){
//        return highScoreConfig;
//    }

//    public int getHighScore(int rows, int cols, int totalItems){
//        int r = doesHighScoreConfigExists(rows, cols, totalItems);
//        if(r != -1){
//            return highScoreConfig[r][COL_HIGH_SCORE];
//        }
//        // returns -1 if config doesn't exist
//        return -1;
//    }

//    public int doesHighScoreConfigExists(int rows, int cols, int totalItems){
//        for(int r = 0; r < highScoreConfig.length; r++){
//            if(highScoreConfig[r][COL_ROWS] == rows
//                    && highScoreConfig[r][COL_COLS] == cols
//                    && highScoreConfig[r][COL_TOTAL_ITEMS] == totalItems){
//                return r;
//            }
//        }
//        // returns -1 if config doesnt exist
//        return -1;
//    }

    public void setParams(int rows, int cols, int totalItems){
        this.rows = rows;
        this.cols = cols;
        this.totalItems = totalItems;
        this.items = new boolean[rows][cols];
    }

    public void setItemValue(int row, int col, boolean value) {
        items[row][col] = value;
    }

//    public void setHighScoreConfig(int[][] highScoreConfig){
//        this.highScoreConfig = highScoreConfig;
//    }

//    public void setHighScoreConfigParams(int sizeOptions, int totalItemsOptions) {
//        // int[][] --> each row indicates a new game configuration
//        // each row contains: number of rows, number of cols, number of items, highscore
//        this.highScoreConfig = new int[sizeOptions * totalItemsOptions][4];
//    }

    public void fillArray(){
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

//    public void createNewHighScoreConfig(int rows, int cols, int totalItems, int score){
//        // Creates new config entry
//        int row = 0;
//        while(highScoreConfig[row][COL_ROWS] != 0){
//            row++;
//        }
//        highScoreConfig[row][COL_ROWS] = rows;
//        highScoreConfig[row][COL_COLS] = cols;
//        highScoreConfig[row][COL_TOTAL_ITEMS] = totalItems;
//        highScoreConfig[row][COL_HIGH_SCORE] = score;
//    }
//
//    public void setHighScore(int rows, int cols, int totalItems, int score){
//        for(int r = 0; r < highScoreConfig.length; r++){
//            if(highScoreConfig[r][COL_ROWS] == rows
//                    && highScoreConfig[r][COL_COLS] == cols
//                    && highScoreConfig[r][COL_TOTAL_ITEMS] == totalItems){
//                if(highScoreConfig[r][COL_HIGH_SCORE] > score){
//                    highScoreConfig[r][COL_HIGH_SCORE] = score;
//                    return;
//                }
//            }
//        }
//
//    }

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

}
