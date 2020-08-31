package project.game.reeldeal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTileTest {

    @Test
    void testCreateGameTile(){
        GameTile gameTile = new GameTile();
        assertAll(
                () -> assertFalse(gameTile.isFishThere()),
                () -> assertFalse(gameTile.isFishRevealed()),
                () -> assertTrue(gameTile.isClickable())
        );
    }

    @Test
    void testSetGameTile(){
        GameTile gameTile = new GameTile();
        gameTile.setFishThere(true);
        gameTile.setFishRevealed(true);
        gameTile.setClicked();
        assertAll(
                () -> assertTrue(gameTile.isFishThere()),
                () -> assertTrue(gameTile.isFishRevealed()),
                () -> assertFalse(gameTile.isClickable())
        );
    }
}