package noughtsandcrosses;

import java.util.Objects;
import java.util.Optional;

public final class GameOptions {

    private final Symbol humanSymbol;
    private final boolean humanFirst;
    private final Optional<ComputerPlayer> computer;

    private GameOptions(Symbol humanSymbol, boolean humanFirst, Optional<ComputerPlayer> computer) {
        this.humanSymbol = humanSymbol;
        this.humanFirst = humanFirst;
        this.computer = computer;
    }

    public static GameOptions humanVsHuman() {
        return new GameOptions(Symbol.X, true, Optional.empty());
    }

    public static GameOptions versusComputer(Symbol humanSymbol, boolean humanFirst, ComputerPlayer computer) {
        Objects.requireNonNull(humanSymbol, "humanSymbol");
        Objects.requireNonNull(computer, "computer");
        return new GameOptions(humanSymbol, humanFirst, Optional.of(computer));
    }

    public boolean versusComputer() {
        return computer.isPresent();
    }

    public Optional<ComputerPlayer> computer() {
        return computer;
    }

    public Symbol humanSymbol() {
        return humanSymbol;
    }

    public Symbol computerSymbol() {
        return humanSymbol.opponent();
    }

    public Symbol startingPlayer() {
        return humanFirst ? humanSymbol : humanSymbol.opponent();
    }
}
