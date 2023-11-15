package iuh.fit.trainingsystembackend.enums;

public enum SectionType {
    elective("Elective"),
    compulsory("Compulsory")
    ;

    private String value = "";

    public String getValue() {
        return value;
    }

    SectionType(String value) {
        this.value = value;
    }
}
