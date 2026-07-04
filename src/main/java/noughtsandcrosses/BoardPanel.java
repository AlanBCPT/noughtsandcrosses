package noughtsandcrosses;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class BoardPanel extends JPanel {

    private static final Font  SYMBOL_FONT                = new Font("SansSerif", Font.BOLD, 64);
    private static final Color GRID_BORDER_COLOR          = new Color(160, 160, 175);
    private static final Color CELL_DEFAULT_BACKGROUND    = new Color(250, 250, 252);
    private static final Color PLAYER_X_COLOR             = new Color(59,  130, 220);
    private static final Color PLAYER_O_COLOR             = new Color(220, 70,  70);
    private static final Color WINNING_CELL_HIGHLIGHT     = new Color(180, 235, 180);
    private static final int   GRID_GAP                   = 5;

    @FunctionalInterface
    public interface MoveListener {
        void onMove(int square);
    }

    private final JButton[] cells = new JButton[Board.SIZE];

    public BoardPanel(MoveListener listener) {
        setLayout(new GridLayout(3, 3, GRID_GAP, GRID_GAP));
        setBackground(GRID_BORDER_COLOR);
        setBorder(BorderFactory.createLineBorder(GRID_BORDER_COLOR, GRID_GAP));
        for (int i = 0; i < Board.SIZE; i++) {
            final int square = i + 1;
            cells[i] = buildCell();
            cells[i].addActionListener(e -> listener.onMove(square));
            add(cells[i]);
        }
    }

    public void refresh(Board board) {
        for (int square = 1; square <= Board.SIZE; square++) {
            Optional<Symbol> symbol = board.symbolAt(square);
            if (symbol.isPresent()) {
                applySymbol(cells[square - 1], symbol.get());
            }
        }
    }

    public void highlightWinners(int[] winLine) {
        for (int index : winLine) {
            cells[index].setBackground(WINNING_CELL_HIGHLIGHT);
        }
    }

    public void disableAll() {
        for (JButton cell : cells) {
            cell.setEnabled(false);
        }
    }

    public void enableAvailable(Board board) {
        for (int square = 1; square <= Board.SIZE; square++) {
            cells[square - 1].setEnabled(board.isAvailable(square));
        }
    }

    public void reset() {
        for (JButton cell : cells) {
            cell.setText("");
            cell.setBackground(CELL_DEFAULT_BACKGROUND);
            cell.setEnabled(true);
        }
    }

    private void applySymbol(JButton cell, Symbol symbol) {
        cell.setText(symbol.name());
        cell.setForeground(symbol == Symbol.X ? PLAYER_X_COLOR : PLAYER_O_COLOR);
        cell.setEnabled(false);
    }

    private JButton buildCell() {
        JButton cell = new JButton();
        cell.setFont(SYMBOL_FONT);
        cell.setBackground(CELL_DEFAULT_BACKGROUND);
        cell.setFocusPainted(false);
        cell.setBorderPainted(false);
        cell.setOpaque(true);
        return cell;
    }
}
