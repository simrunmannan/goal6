package team.model.enums;

public enum  PurityCondition {
    SAFE("Safe"),
    TREATABLE("Treatable"),
    UNSAFE("Unsafe");

    private final String string;

    PurityCondition(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
