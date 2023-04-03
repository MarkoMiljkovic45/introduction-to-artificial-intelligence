package ui.model;

public enum CommandType {
    ADD("+"),
    REMOVE("-"),
    QUERY("?");

    private final String symbol;

    CommandType(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
