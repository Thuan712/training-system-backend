package iuh.fit.trainingsystembackend.enums;

public enum TermType {
    first_term ("Học kì đầu") , second_term ("Học kỳ hai"), summer_term ("Học kỳ hè");

    private String value;

    TermType(String value) {
        this.value = value;
    }
}
