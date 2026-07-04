package noughtsandcrosses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Board {

    public static final int SIZE = 9;

    private static final int[][] WIN_LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    private final Symbol[] cells;

    public Board() {
        this(new Symbol[SIZE]);
    }

    private Board(Symbol[] cells) {
        this.cells = cells;
    }

    public Board place(int cell, Symbol symbol) {
        Objects.requireNonNull(symbol, "symbol");
        requireInBounds(cell);
        if (cells[cell] != null) {
            throw new IllegalStateException("Cell " + cell + " is already taken");
        }
        Symbol[] next = cells.clone();
        next[cell] = symbol;
        return new Board(next);
    }

    public boolean isAvailable(int cell) {
        requireInBounds(cell);
        return cells[cell] == null;
    }

    public Optional<Symbol> symbolAt(int cell) {
        requireInBounds(cell);
        return Optional.ofNullable(cells[cell]);
    }

    public List<Integer> availableCells() {
        List<Integer> available = new ArrayList<>();
        for (int cell = 0; cell < SIZE; cell++) {
            if (cells[cell] == null) available.add(cell);
        }
        return available;
    }

    public boolean isFull() {
        for (Symbol cell : cells) {
            if (cell == null) return false;
        }
        return true;
    }

    public Optional<Symbol> winner() {
        return winningLine().map(line -> cells[line[0]]);
    }

    public Optional<int[]> winningLine() {
        for (int[] line : WIN_LINES) {
            Symbol first = cells[line[0]];
            if (first != null && first == cells[line[1]] && first == cells[line[2]]) {
                return Optional.of(line.clone());
            }
        }
        return Optional.empty();
    }

    private static void requireInBounds(int cell) {
        if (cell < 0 || cell >= SIZE) {
            throw new IndexOutOfBoundsException("Cell out of range: " + cell);
        }
    }
}
