package iuh.fit.trainingsystembackend.enums;

public enum CourseType {
    elective("Elective"),
    compulsory("Compulsory")
    ;

    private String value = "";

    public String getValue() {
        return value;
    }

    CourseType(String value) {
        this.value = value;
    }
}
