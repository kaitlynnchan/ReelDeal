package project.game.reeldeal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testCreateGame(){
        Game game = new Game(5, 10, 20, 23);
        assertAll(
                () -> assertEquals(5, game.getRows()),
                () -> assertEquals(10, game.getColumns()),
                () -> assertEquals(20, game.getTotalFishes()),
                () -> assertEquals(23, game.getHighScore())
        );
    }

    @Test
    void testCreateArray(){
        Game game = new Game(5, 10, 20, 23);
        game.fillArray();

    }
}