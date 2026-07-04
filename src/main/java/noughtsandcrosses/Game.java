package noughtsandcrosses;

import java.util.Optional;

public class Game {

    public enum Result { IN_PROGRESS, X_WINS, O_WINS, DRAW }

    private final Board board = new Board();
    private Symbol currentPlayer;

    public Game(Symbol startingPlayer) {
        this.currentPlayer = startingPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Symbol getCurrentPlayer() {
        return currentPlayer;
    }

    public Result takeTurn(int square) {
        board.place(square, currentPlayer);
        Result result = determineResult();
        if (result == Result.IN_PROGRESS) {
            currentPlayer = currentPlayer.opponent();
        }
        return result;
    }

    private Result determineResult() {
        Optional<Symbol> winner = board.winner();
        if (winner.isPresent()) {
            return winner.get() == Symbol.X ? Result.X_WINS : Result.O_WINS;
        }
        return board.isFull() ? Result.DRAW : Result.IN_PROGRESS;
    }
}
