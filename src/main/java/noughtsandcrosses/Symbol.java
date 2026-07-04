package noughtsandcrosses;

public enum Symbol {
    X, O;

    public Symbol opponent() {
        return this == X ? O : X;
    }
}
