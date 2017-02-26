package team.model.enums;

public enum SourceCondition {
    WASTE("Waste"),
    TREATABLE_CLEAR("Treatable-Clear"),
    TREATABLE_MUDDY("Treatable-Muddy"),
    POTABLE("Potable");

    private final String string;

    SourceCondition(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
