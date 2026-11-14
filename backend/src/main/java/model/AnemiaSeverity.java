package model;

public enum AnemiaSeverity {
    NORMAL("Normal"),
    MILD("Mild"),
    MODERATE("Moderate"),
    SEVERE("Severe");

    private final String label;

    AnemiaSeverity(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}