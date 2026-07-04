package noughtsandcrosses;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void newBoardHasEveryCellAvailable() {
        Board board = new Board();
        for (int cell = 0; cell < Board.SIZE; cell++) {
            assertTrue(board.isAvailable(cell));
            assertEquals(Optional.empty(), board.symbolAt(cell));
        }
        assertEquals(9, board.availableCells().size());
        assertFalse(board.isFull());
        assertEquals(Optional.empty(), board.winner());
        assertEquals(Optional.empty(), board.winningLine());
    }

    @Test
    void placingLeavesTheOriginalBoardUnchanged() {
        Board original = new Board();
        Board next = original.place(4, Symbol.X);

        assertTrue(original.isAvailable(4));
        assertEquals(Optional.of(Symbol.X), next.symbolAt(4));
        assertFalse(next.isAvailable(4));
    }

    @Test
    void placingRemovesTheCellFromAvailable() {
        Board board = new Board().place(0, Symbol.X);
        List<Integer> available = board.availableCells();

        assertEquals(8, available.size());
        assertFalse(available.contains(0));
    }

    @Test
    void placingOnATakenCellIsRejected() {
        Board board = new Board().place(0, Symbol.X);
        assertThrows(IllegalStateException.class, () -> board.place(0, Symbol.O));
    }

    @Test
    void placingOutOfBoundsIsRejected() {
        Board board = new Board();
        assertThrows(IndexOutOfBoundsException.class, () -> board.place(9, Symbol.X));
        assertThrows(IndexOutOfBoundsException.class, () -> board.place(-1, Symbol.X));
    }

    @Test
    void placingNullSymbolIsRejected() {
        Board board = new Board();
        assertThrows(NullPointerException.class, () -> board.place(0, null));
    }

    @Test
    void queryingOutOfBoundsIsRejected() {
        Board board = new Board();
        assertThrows(IndexOutOfBoundsException.class, () -> board.isAvailable(9));
        assertThrows(IndexOutOfBoundsException.class, () -> board.symbolAt(-1));
    }

    @Test
    void detectsWinningRow() {
        Board board = line(new int[]{0, 1, 2}, Symbol.X);
        assertEquals(Optional.of(Symbol.X), board.winner());
        assertArrayEquals(new int[]{0, 1, 2}, board.winningLine().orElseThrow());
    }

    @Test
    void detectsWinningColumn() {
        Board board = line(new int[]{2, 5, 8}, Symbol.O);
        assertEquals(Optional.of(Symbol.O), board.winner());
        assertArrayEquals(new int[]{2, 5, 8}, board.winningLine().orElseThrow());
    }

    @Test
    void detectsWinningDiagonal() {
        Board board = line(new int[]{0, 4, 8}, Symbol.X);
        assertEquals(Optional.of(Symbol.X), board.winner());
        assertArrayEquals(new int[]{0, 4, 8}, board.winningLine().orElseThrow());
    }

    @Test
    void detectsWinningAntiDiagonal() {
        Board board = line(new int[]{2, 4, 6}, Symbol.O);
        assertEquals(Optional.of(Symbol.O), board.winner());
        assertArrayEquals(new int[]{2, 4, 6}, board.winningLine().orElseThrow());
    }

    @Test
    void winningLineIsADefensiveCopy() {
        Board board = line(new int[]{0, 1, 2}, Symbol.X);
        int[] first = board.winningLine().orElseThrow();
        first[0] = 99;
        assertArrayEquals(new int[]{0, 1, 2}, board.winningLine().orElseThrow());
    }

    @Test
    void fullBoardWithNoLineIsADraw() {
        // X O X / X O O / O X X  — full, no three in a row
        Board board = new Board()
            .place(0, Symbol.X).place(1, Symbol.O).place(2, Symbol.X)
            .place(3, Symbol.X).place(4, Symbol.O).place(5, Symbol.O)
            .place(6, Symbol.O).place(7, Symbol.X).place(8, Symbol.X);

        assertTrue(board.isFull());
        assertEquals(Optional.empty(), board.winner());
        assertEquals(Optional.empty(), board.winningLine());
        assertTrue(board.availableCells().isEmpty());
    }

    private static Board line(int[] cells, Symbol symbol) {
        Board board = new Board();
        for (int cell : cells) {
            board = board.place(cell, symbol);
        }
        return board;
    }
}
