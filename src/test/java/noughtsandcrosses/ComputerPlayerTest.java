package noughtsandcrosses;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerPlayerTest {

    @Test
    void easyOnlyEverPicksAnAvailableCell() {
        Board board = new Board().place(0, Symbol.X).place(4, Symbol.O);
        ComputerPlayer easy = new ComputerPlayer(Difficulty.EASY, new Random(1));

        for (int i = 0; i < 50; i++) {
            int move = easy.chooseMove(board, Symbol.X);
            assertTrue(board.isAvailable(move), "chose taken cell " + move);
        }
    }

    @Test
    void easyPicksTheOnlyRemainingCell() {
        Board board = new Board();
        for (int cell = 0; cell < Board.SIZE - 1; cell++) {
            board = board.place(cell, Symbol.X);
        }
        ComputerPlayer easy = new ComputerPlayer(Difficulty.EASY, new Random());
        assertEquals(8, easy.chooseMove(board, Symbol.O));
    }

    @Test
    void mediumTakesAnImmediateWin() {
        // O at 0,1 — winning move is 2. A block is also available at X's 3,4 -> 5.
        Board board = new Board()
            .place(0, Symbol.O).place(1, Symbol.O)
            .place(3, Symbol.X).place(4, Symbol.X);
        ComputerPlayer medium = new ComputerPlayer(Difficulty.MEDIUM, new Random());
        assertEquals(2, medium.chooseMove(board, Symbol.O));
    }

    @Test
    void mediumBlocksAnImmediateLoss() {
        // X threatens to complete 0,1,2 at cell 2; O has no win of its own.
        Board board = new Board()
            .place(0, Symbol.X).place(1, Symbol.X)
            .place(4, Symbol.O);
        ComputerPlayer medium = new ComputerPlayer(Difficulty.MEDIUM, new Random());
        assertEquals(2, medium.chooseMove(board, Symbol.O));
    }

    @Test
    void mediumFallsBackToARandomAvailableCell() {
        Board board = new Board();
        ComputerPlayer medium = new ComputerPlayer(Difficulty.MEDIUM, new Random(7));
        int move = medium.chooseMove(board, Symbol.X);
        assertTrue(board.isAvailable(move));
    }

    @Test
    void hardTakesAnImmediateWin() {
        Board board = new Board()
            .place(0, Symbol.X).place(1, Symbol.X)
            .place(4, Symbol.O).place(8, Symbol.O);
        ComputerPlayer hard = new ComputerPlayer(Difficulty.HARD);
        assertEquals(2, hard.chooseMove(board, Symbol.X));
    }

    @Test
    void hardBlocksAnImmediateLoss() {
        // O threatens 0,1,2 at cell 2; X must block there (no win of its own yet).
        Board board = new Board()
            .place(0, Symbol.O).place(1, Symbol.O)
            .place(4, Symbol.X);
        ComputerPlayer hard = new ComputerPlayer(Difficulty.HARD);
        assertEquals(2, hard.chooseMove(board, Symbol.X));
    }

    @Test
    void hardChoosesAValidOpeningMove() {
        ComputerPlayer hard = new ComputerPlayer(Difficulty.HARD);
        int move = hard.chooseMove(new Board(), Symbol.X);
        assertTrue(move >= 0 && move < Board.SIZE);
    }

    @Test
    void hardNeverLosesEvenWhenMovingSecond() {
        // A perfect player, moving second against every possible opponent line,
        // must at worst force a draw.
        for (int opponentOpening = 0; opponentOpening < Board.SIZE; opponentOpening++) {
            Symbol result = playPerfectAgainstRandom(opponentOpening);
            assertNotEquals(Symbol.X, result, "perfect O lost to opening " + opponentOpening);
        }
    }

    // Plays O as a perfect player against an X that opens at the given cell and
    // then plays randomly; returns the winner, or null for a draw.
    private Symbol playPerfectAgainstRandom(int opening) {
        ComputerPlayer perfect = new ComputerPlayer(Difficulty.HARD);
        Random opponent = new Random(opening);
        Game game = new Game(Symbol.X);

        Game.Result result = game.takeTurn(opening);
        while (result == Game.Result.IN_PROGRESS) {
            int move = game.currentPlayer() == Symbol.O
                ? perfect.chooseMove(game.board(), Symbol.O)
                : randomAvailable(game.board(), opponent);
            result = game.takeTurn(move);
        }
        switch (result) {
            case X_WINS: return Symbol.X;
            case O_WINS: return Symbol.O;
            default:     return null;
        }
    }

    private int randomAvailable(Board board, Random random) {
        var available = board.availableCells();
        return available.get(random.nextInt(available.size()));
    }
}
