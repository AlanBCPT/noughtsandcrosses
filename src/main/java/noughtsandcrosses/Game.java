package noughtsandcrosses;

import java.util.Objects;
import java.util.Optional;

public final class Game {

    public enum Result { IN_PROGRESS, X_WINS, O_WINS, DRAW }

    private Board board;
    private Symbol currentPlayer;

    public Game(Symbol startingPlayer) {
        this.board = new Board();
        this.currentPlayer = Objects.requireNonNull(startingPlayer, "startingPlayer");
    }

    public Board board() {
        return board;
    }

    public Symbol currentPlayer() {
        return currentPlayer;
    }

    public Result takeTurn(int cell) {
        board = board.place(cell, currentPlayer);
        Result result = resultOf(board);
        if (result == Result.IN_PROGRESS) {
            currentPlayer = currentPlayer.opponent();
        }
        return result;
    }

    private static Result resultOf(Board board) {
        Optional<Symbol> winner = board.winner();
        if (winner.isPresent()) {
            return winner.get() == Symbol.X ? Result.X_WINS : Result.O_WINS;
        }
        return board.isFull() ? Result.DRAW : Result.IN_PROGRESS;
    }
}
