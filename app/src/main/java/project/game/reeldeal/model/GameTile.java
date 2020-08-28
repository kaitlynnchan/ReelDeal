package project.game.reeldeal.model;

/**
 * GAME TILE CLASS
 * Stores information about a game tile
 */
public class GameTile {

    private boolean isFishThere;
    private boolean isFishRevealed;
    private boolean isClickable;

    public GameTile() {
        this.isFishThere = false;       // Set to false by default
        this.isFishRevealed = false;    // Set to false by default
        this.isClickable = true;        // Set to true by default
    }

    public boolean isFishThere() {
        return isFishThere;
    }

    public void setFishThere(boolean fishThere) {
        isFishThere = fishThere;
    }

    public boolean isFishRevealed() {
        return isFishRevealed;
    }

    public void setFishRevealed(boolean fishRevealed) {
        isFishRevealed = fishRevealed;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClicked() {
        isClickable = false;
    }
}
