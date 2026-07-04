package noughtsandcrosses;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SymbolTest {

    @Test
    void opponentOfXisO() {
        assertEquals(Symbol.O, Symbol.X.opponent());
    }

    @Test
    void opponentOfOisX() {
        assertEquals(Symbol.X, Symbol.O.opponent());
    }

    @Test
    void opponentIsItsOwnInverse() {
        assertEquals(Symbol.X, Symbol.X.opponent().opponent());
        assertEquals(Symbol.O, Symbol.O.opponent().opponent());
    }
}
