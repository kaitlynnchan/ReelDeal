package cmpt276.assign3.assign3game.model;

/**
 * FishesManager class
 * Stores a 2D array of booleans, total fishes, and high score
 * Basic logic:~
 *      Value of boolean legend:
 *          true = fish present
 *          false = fish not present
 */
public class FishesManager {
    private boolean[][] fishes;
    private int rows;
    private int cols;
    private int totalFishes;
    private int highScore;

    // Singleton implementation of FishesManager
    private static FishesManager instance;
    private FishesManager(){/*to prevent others from instantiating*/}
    public static FishesManager getInstance(){
        if(instance == null){
            instance = new FishesManager();
        }
        return instance;
    }

    public boolean[][] getArray() {
        return fishes;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalFishes() {
        return totalFishes;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setArray(boolean[][] fishes) {
        this.fishes = fishes;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setTotalFishes(int totalFishes) {
        this.totalFishes = totalFishes;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setArrayIndexValue(int row, int col, boolean value) {
        fishes[row][col] = value;
    }

    public void fillArray(){
        fishes = new boolean[rows][cols];

        // Randomly add fishTotal i.e. amount of fishes into array
        int tempTotalFishes = totalFishes;
        while(tempTotalFishes > 0){
            int row = (int) ( Math.random() * rows );
            int col = (int) ( Math.random() * cols );
            if(!isFishThere(row, col)) {
                fishes[row][col] = true;
                tempTotalFishes--;
            }
        }
    }

    public int scanRowCol(int row, int col){
        // return -1 if the position in the array has a fish
        if(isFishThere(row, col)){
            return -1;
        }

        int fishTracker = 0;
        for(int r = 0; r < rows; r++){
            if(isFishThere(r, col)){
                fishTracker++;
            }
        }
        for(int c = 0; c < cols; c++){
            if(isFishThere(row, c)){
                fishTracker++;
            }
        }
        return fishTracker;
    }

    private boolean isFishThere(int row, int col){
        return fishes[row][col];
    }

}