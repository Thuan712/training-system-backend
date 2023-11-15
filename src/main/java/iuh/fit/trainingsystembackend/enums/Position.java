package iuh.fit.trainingsystembackend.enums;

public enum Position {
    teacher("Teacher"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    Position(String value) {
        this.value = value;
    }
}
