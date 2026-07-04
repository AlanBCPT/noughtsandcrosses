package noughtsandcrosses;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ComputerPlayer {

    private static final int WIN_SCORE  =  10;
    private static final int LOSS_SCORE = -10;
    private static final int DRAW_SCORE =   0;

    private final Symbol symbol;
    private final Difficulty difficulty;
    private final Random random = new Random();

    public ComputerPlayer(Symbol symbol, Difficulty difficulty) {
        this.symbol = symbol;
        this.difficulty = difficulty;
    }

    public int chooseMove(Board board) {
        switch (difficulty) {
            case EASY:   return randomMove(board);
            case MEDIUM: return tacticalMove(board);
            default:     return optimalMove(board);
        }
    }

    private int randomMove(Board board) {
        List<Integer> squares = board.availableSquares();
        return squares.get(random.nextInt(squares.size()));
    }

    private int tacticalMove(Board board) {
        Optional<Integer> winningSquare = winningSquare(board, symbol);
        if (winningSquare.isPresent()) return winningSquare.get();

        Optional<Integer> blockingSquare = winningSquare(board, symbol.opponent());
        if (blockingSquare.isPresent()) return blockingSquare.get();

        return randomMove(board);
    }

    private Optional<Integer> winningSquare(Board board, Symbol player) {
        for (int square : board.availableSquares()) {
            board.place(square, player);
            boolean wins = board.winner().isPresent();
            board.undo(square);
            if (wins) return Optional.of(square);
        }
        return Optional.empty();
    }

    private int optimalMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int bestSquare = -1;
        for (int square : board.availableSquares()) {
            board.place(square, symbol);
            int score = minimax(board, false);
            board.undo(square);
            if (score > bestScore) {
                bestScore = score;
                bestSquare = square;
            }
        }
        return bestSquare;
    }

    private int minimax(Board board, boolean isMaximizing) {
        Optional<Symbol> winner = board.winner();
        if (winner.isPresent()) return winner.get() == symbol ? WIN_SCORE : LOSS_SCORE;
        if (board.isFull()) return DRAW_SCORE;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int square : board.availableSquares()) {
                board.place(square, symbol);
                bestScore = Math.max(bestScore, minimax(board, false));
                board.undo(square);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int square : board.availableSquares()) {
                board.place(square, symbol.opponent());
                bestScore = Math.min(bestScore, minimax(board, true));
                board.undo(square);
            }
            return bestScore;
        }
    }
}
