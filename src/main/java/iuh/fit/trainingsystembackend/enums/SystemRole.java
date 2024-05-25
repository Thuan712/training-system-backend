package iuh.fit.trainingsystembackend.enums;

public enum SystemRole {
    student("Student"),
    lecturer("Lecturer"),
    staff("Staff"),
    admin("Admin");

    private String value;

    public String getValue() {
        return value;
    }

    SystemRole(String value) {
        this.value = value;
    }
}
