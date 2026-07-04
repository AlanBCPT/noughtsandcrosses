package noughtsandcrosses;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

    public static final int SIZE = 9;

    private static final char EMPTY_CELL_BASE = '1';

    private static final int[][] WIN_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    private final char[] cells = new char[SIZE];

    public Board() {
        for (int i = 0; i < SIZE; i++) {
            cells[i] = (char) (EMPTY_CELL_BASE + i);
        }
    }

    public boolean isAvailable(int square) {
        return Character.isDigit(cells[square - 1]);
    }

    public void place(int square, Symbol symbol) {
        cells[square - 1] = symbol.name().charAt(0);
    }

    public void undo(int square) {
        cells[square - 1] = (char) (EMPTY_CELL_BASE + square - 1);
    }

    public Optional<Symbol> winner() {
        return findWinningLine().flatMap(line -> symbolAtIndex(line[0]));
    }

    public Optional<int[]> winningLine() {
        return findWinningLine();
    }

    public boolean isFull() {
        for (char cell : cells) {
            if (Character.isDigit(cell)) return false;
        }
        return true;
    }

    public List<Integer> availableSquares() {
        List<Integer> squares = new ArrayList<>();
        for (int square = 1; square <= SIZE; square++) {
            if (isAvailable(square)) squares.add(square);
        }
        return squares;
    }

    public Optional<Symbol> symbolAt(int square) {
        return symbolAtIndex(square - 1);
    }

    private Optional<int[]> findWinningLine() {
        for (int[] line : WIN_LINES) {
            if (cells[line[0]] == cells[line[1]] && cells[line[1]] == cells[line[2]]) {
                return Optional.of(line.clone());
            }
        }
        return Optional.empty();
    }

    private Optional<Symbol> symbolAtIndex(int index) {
        char cell = cells[index];
        if (cell == 'X') return Optional.of(Symbol.X);
        if (cell == 'O') return Optional.of(Symbol.O);
        return Optional.empty();
    }

    @Override
    public String toString() {
        return String.format(
            " %s | %s | %s %n---+---+---%n %s | %s | %s %n---+---+---%n %s | %s | %s %n",
            cells[0], cells[1], cells[2],
            cells[3], cells[4], cells[5],
            cells[6], cells[7], cells[8]
        );
    }
}
