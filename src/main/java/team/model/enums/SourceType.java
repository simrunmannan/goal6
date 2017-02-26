package team.model.enums;

public enum SourceType {
    BOTTLED("Bottled"),
    WELL("Well"),
    STREAM("Stream"),
    LAKE("Lake"),
    SPRING("Spring"),
    OTHER("Other");

    private final String string;

    SourceType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
