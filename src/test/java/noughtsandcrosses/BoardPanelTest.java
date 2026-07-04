package noughtsandcrosses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class BoardPanelTest {

    @BeforeEach
    void requireDisplay() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "no display available");
    }

    private JButton cell(BoardPanel panel, int index) {
        return (JButton) panel.getComponent(index);
    }

    @Test
    void renderShowsPlacedSymbolsAndLeavesEmptyCellsBlank() {
        BoardPanel panel = new BoardPanel(ignored -> { });
        panel.render(new Board().place(0, Symbol.X).place(4, Symbol.O));

        assertEquals("X", cell(panel, 0).getText());
        assertEquals("O", cell(panel, 4).getText());
        assertEquals("", cell(panel, 1).getText());
    }

    @Test
    void enableAvailableEnablesEmptyCellsOnly() {
        BoardPanel panel = new BoardPanel(ignored -> { });
        panel.enableAvailable(new Board().place(0, Symbol.X));

        assertFalse(cell(panel, 0).isEnabled());
        assertTrue(cell(panel, 1).isEnabled());
    }

    @Test
    void disableAllDisablesEveryCell() {
        BoardPanel panel = new BoardPanel(ignored -> { });
        panel.disableAll();

        for (int index = 0; index < Board.SIZE; index++) {
            assertFalse(cell(panel, index).isEnabled());
        }
    }

    @Test
    void highlightWinnersChangesOnlyTheWinningCells() {
        BoardPanel panel = new BoardPanel(ignored -> { });
        panel.render(new Board());
        panel.highlightWinners(new int[]{0, 1, 2});

        assertEquals(cell(panel, 0).getBackground(), cell(panel, 2).getBackground());
        assertNotEquals(cell(panel, 0).getBackground(), cell(panel, 3).getBackground());
    }

    @Test
    void clickingACellReportsItsIndex() {
        int[] clicked = {-1};
        BoardPanel panel = new BoardPanel(cell -> clicked[0] = cell);

        cell(panel, 5).doClick();

        assertEquals(5, clicked[0]);
    }
}
