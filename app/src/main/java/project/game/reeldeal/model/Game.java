package project.game.reeldeal.model;

/**
 * GAME CLASS
 * Stores a collection of game tiles to form a game board,
 *  dimensions of the game board, high score
 */
public class Game {

    private GameTile[][] gameBoard;
    private int rows;
    private int columns;
    private int totalFishes;
    private int highScore;

    public Game(int rows, int columns, int totalFishes, int highScore) {
        this.rows = rows;
        this.columns = columns;
        this.totalFishes = totalFishes;
        this.highScore = highScore;
    }

    public GameTile[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameTile[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getTotalFishes() {
        return totalFishes;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public GameTile getTile(int row, int col){
        return gameBoard[row][col];
    }

    public void fillArray(){
        gameBoard = new GameTile[rows][columns];
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                gameBoard[row][col] = new GameTile();
            }
        }

        // Randomly add fishTotal i.e. amount of fishes into array
        int tempTotalFishes = totalFishes;
        while(tempTotalFishes > 0){
            int row = (int) ( Math.random() * rows );
            int col = (int) ( Math.random() * columns);
            if(!isFishThere(row, col)) {
                gameBoard[row][col].setFishThere(true);
                tempTotalFishes--;
            }
        }
    }

    // Return -1 if there is a fish at specified position
    //  else returns number of fishes in row and col
    public int scanRowCol(int row, int col){
        if(isFishThere(row, col) && !gameBoard[row][col].isFishRevealed()){
            return -1;
        }

        int fishTracker = 0;
        for(int r = 0; r < rows; r++){
            if(isFishThere(r, col) && !gameBoard[r][col].isFishRevealed()){
                fishTracker++;
            }
        }
        for(int c = 0; c < columns; c++){
            if(isFishThere(row, c) && !gameBoard[row][c].isFishRevealed()){
                fishTracker++;
            }
        }
        return fishTracker;
    }

    private boolean isFishThere(int row, int col){
        return gameBoard[row][col].isFishThere();
    }
}