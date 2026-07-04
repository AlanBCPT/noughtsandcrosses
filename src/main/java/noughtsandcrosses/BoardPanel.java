package noughtsandcrosses;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public final class BoardPanel extends JPanel {

    @FunctionalInterface
    public interface MoveListener {
        void onMove(int cell);
    }

    private static final Font  SYMBOL_FONT             = new Font("SansSerif", Font.BOLD, 64);
    private static final Color GRID_BORDER_COLOR       = new Color(160, 160, 175);
    private static final Color CELL_DEFAULT_BACKGROUND = new Color(250, 250, 252);
    private static final Color PLAYER_X_COLOR          = new Color(59,  130, 220);
    private static final Color PLAYER_O_COLOR          = new Color(220, 70,  70);
    private static final Color WINNING_CELL_HIGHLIGHT  = new Color(180, 235, 180);
    private static final int   GRID_GAP                = 5;

    private final JButton[] cells = new JButton[Board.SIZE];

    public BoardPanel(MoveListener listener) {
        setLayout(new GridLayout(3, 3, GRID_GAP, GRID_GAP));
        setBackground(GRID_BORDER_COLOR);
        setBorder(BorderFactory.createLineBorder(GRID_BORDER_COLOR, GRID_GAP));
        for (int cell = 0; cell < Board.SIZE; cell++) {
            cells[cell] = buildCell(cell, listener);
            add(cells[cell]);
        }
    }

    public void render(Board board) {
        for (int cell = 0; cell < Board.SIZE; cell++) {
            paintCell(cells[cell], board.symbolAt(cell));
        }
    }

    public void enableAvailable(Board board) {
        for (int cell = 0; cell < Board.SIZE; cell++) {
            cells[cell].setEnabled(board.isAvailable(cell));
        }
    }

    public void disableAll() {
        for (JButton cell : cells) {
            cell.setEnabled(false);
        }
    }

    public void highlightWinners(int[] winningLine) {
        for (int cell : winningLine) {
            cells[cell].setBackground(WINNING_CELL_HIGHLIGHT);
        }
    }

    private void paintCell(JButton button, Optional<Symbol> symbol) {
        button.setText(symbol.map(Symbol::name).orElse(""));
        button.setForeground(symbol.map(BoardPanel::colorFor).orElse(Color.BLACK));
        button.setBackground(CELL_DEFAULT_BACKGROUND);
    }

    private static Color colorFor(Symbol symbol) {
        return symbol == Symbol.X ? PLAYER_X_COLOR : PLAYER_O_COLOR;
    }

    private JButton buildCell(int cell, MoveListener listener) {
        JButton button = new JButton();
        button.setFont(SYMBOL_FONT);
        button.setBackground(CELL_DEFAULT_BACKGROUND);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.addActionListener(e -> listener.onMove(cell));
        return button;
    }
}
