package iuh.fit.trainingsystembackend.enums;


public enum TrainingLevel {
    college("College"),
    university("University")
    ;

    private String value;

    public String getValue() {
        return value;
    }

    TrainingLevel(String value) {
        this.value = value;
    }
}
