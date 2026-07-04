package noughtsandcrosses;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public final class ComputerPlayer {

    private static final int WIN_SCORE  =  10;
    private static final int LOSS_SCORE = -10;
    private static final int DRAW_SCORE =   0;

    private final Difficulty difficulty;
    private final Random random;

    public ComputerPlayer(Difficulty difficulty) {
        this(difficulty, new Random());
    }

    ComputerPlayer(Difficulty difficulty, Random random) {
        this.difficulty = difficulty;
        this.random = random;
    }

    public int chooseMove(Board board, Symbol toMove) {
        switch (difficulty) {
            case EASY:   return randomMove(board);
            case MEDIUM: return tacticalMove(board, toMove);
            case HARD:   return optimalMove(board, toMove);
            default:     throw new IllegalStateException("Unknown difficulty: " + difficulty);
        }
    }

    private int randomMove(Board board) {
        List<Integer> available = board.availableCells();
        return available.get(random.nextInt(available.size()));
    }

    private int tacticalMove(Board board, Symbol toMove) {
        Optional<Integer> winningMove = winningMove(board, toMove);
        if (winningMove.isPresent()) return winningMove.get();

        Optional<Integer> blockingMove = winningMove(board, toMove.opponent());
        if (blockingMove.isPresent()) return blockingMove.get();

        return randomMove(board);
    }

    private Optional<Integer> winningMove(Board board, Symbol symbol) {
        for (int cell : board.availableCells()) {
            if (board.place(cell, symbol).winner().isPresent()) return Optional.of(cell);
        }
        return Optional.empty();
    }

    private int optimalMove(Board board, Symbol me) {
        int bestScore = Integer.MIN_VALUE;
        int bestCell = board.availableCells().get(0);
        for (int cell : board.availableCells()) {
            int score = minimax(board.place(cell, me), me, me.opponent());
            if (score > bestScore) {
                bestScore = score;
                bestCell = cell;
            }
        }
        return bestCell;
    }

    private int minimax(Board board, Symbol me, Symbol toMove) {
        Optional<Symbol> winner = board.winner();
        if (winner.isPresent()) return winner.get() == me ? WIN_SCORE : LOSS_SCORE;
        if (board.isFull()) return DRAW_SCORE;

        boolean maximizing = toMove == me;
        int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int cell : board.availableCells()) {
            int score = minimax(board.place(cell, toMove), me, toMove.opponent());
            best = maximizing ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }
}
