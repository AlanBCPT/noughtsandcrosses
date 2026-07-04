package noughtsandcrosses;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameOptionsTest {

    @Test
    void humanVsHumanHasNoComputerAndStartsWithX() {
        GameOptions options = GameOptions.humanVsHuman();

        assertFalse(options.versusComputer());
        assertEquals(Optional.empty(), options.computer());
        assertEquals(Symbol.X, options.startingPlayer());
    }

    @Test
    void versusComputerExposesTheComputerAndDerivesItsSymbol() {
        ComputerPlayer computer = new ComputerPlayer(Difficulty.HARD);
        GameOptions options = GameOptions.versusComputer(Symbol.X, true, computer);

        assertTrue(options.versusComputer());
        assertEquals(Optional.of(computer), options.computer());
        assertEquals(Symbol.X, options.humanSymbol());
        assertEquals(Symbol.O, options.computerSymbol());
    }

    @Test
    void startingPlayerReflectsWhoGoesFirst() {
        ComputerPlayer computer = new ComputerPlayer(Difficulty.EASY);

        assertEquals(Symbol.O, GameOptions.versusComputer(Symbol.O, true, computer).startingPlayer());
        assertEquals(Symbol.X, GameOptions.versusComputer(Symbol.O, false, computer).startingPlayer());
    }

    @Test
    void versusComputerRejectsNullArguments() {
        ComputerPlayer computer = new ComputerPlayer(Difficulty.EASY);
        assertThrows(NullPointerException.class, () -> GameOptions.versusComputer(null, true, computer));
        assertThrows(NullPointerException.class, () -> GameOptions.versusComputer(Symbol.X, true, null));
    }
}
