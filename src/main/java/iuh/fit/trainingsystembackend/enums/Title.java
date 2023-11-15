package iuh.fit.trainingsystembackend.enums;

public enum Title {
    unknown("Unknown"),
    doctor("Doctor"),
    master("Master")
    ;

    private String value = "";

    public String getValue() {
        return value;
    }

    Title(String value) {
        this.value = value;
    }
}
