package ui.model;

public record Command(CommandType type, Clause clause) {

    @Override
    public String toString() {
        return clause + " " + type;
    }
}
