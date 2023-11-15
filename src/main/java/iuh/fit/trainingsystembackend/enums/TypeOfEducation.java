package iuh.fit.trainingsystembackend.enums;

public enum TypeOfEducation {
    general_program("General Program"),
    high_quality_program("High-Quality Training Program ")
    ;

    private String value = "";

    public String getValue() {
        return value;
    }

    TypeOfEducation(String value) {
        this.value = value;
    }
}
