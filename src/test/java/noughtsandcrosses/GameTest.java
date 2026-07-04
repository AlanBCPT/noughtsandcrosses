package noughtsandcrosses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    @Test
    void startsWithTheGivenPlayer() {
        assertEquals(Symbol.X, new Game(Symbol.X).currentPlayer());
        assertEquals(Symbol.O, new Game(Symbol.O).currentPlayer());
    }

    @Test
    void nullStartingPlayerIsRejected() {
        assertThrows(NullPointerException.class, () -> new Game(null));
    }

    @Test
    void takingATurnPlacesTheCurrentPlayerAndHandsOver() {
        Game game = new Game(Symbol.X);

        Game.Result result = game.takeTurn(0);

        assertEquals(Game.Result.IN_PROGRESS, result);
        assertEquals(Symbol.X, game.board().symbolAt(0).orElseThrow());
        assertEquals(Symbol.O, game.currentPlayer());
    }

    @Test
    void replayingATakenCellIsRejected() {
        Game game = new Game(Symbol.X);
        game.takeTurn(0);
        assertThrows(IllegalStateException.class, () -> game.takeTurn(0));
    }

    @Test
    void reportsXWinsAndStopsHandingOver() {
        Game game = new Game(Symbol.X);
        game.takeTurn(0); game.takeTurn(3); // X O
        game.takeTurn(1); game.takeTurn(4); // X O
        Game.Result result = game.takeTurn(2); // X completes top row

        assertEquals(Game.Result.X_WINS, result);
        assertEquals(Symbol.X, game.currentPlayer());
    }

    @Test
    void reportsOWins() {
        Game game = new Game(Symbol.X);
        game.takeTurn(6);            // X
        game.takeTurn(0);            // O
        game.takeTurn(7);            // X
        game.takeTurn(1);            // O
        game.takeTurn(5);            // X
        Game.Result result = game.takeTurn(2); // O completes top row

        assertEquals(Game.Result.O_WINS, result);
    }

    @Test
    void reportsADraw() {
        Game game = new Game(Symbol.X);
        // X O X / X O O / O X X
        int[] order = {0, 1, 2, 4, 3, 5, 7, 6, 8};
        Game.Result result = Game.Result.IN_PROGRESS;
        for (int cell : order) {
            result = game.takeTurn(cell);
        }
        assertEquals(Game.Result.DRAW, result);
    }
}
