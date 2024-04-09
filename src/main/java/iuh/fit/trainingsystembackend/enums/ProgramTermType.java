package iuh.fit.trainingsystembackend.enums;

public enum ProgramTermType {
    term_first("Học kỳ 1"),
    term_second("Học kỳ 2"),
    term_third("Học kỳ 3"),
    term_fourth("Học kỳ 4"),
    term_fifth("Học kỳ 5"),
    term_sixth("Học kỳ 6"),
    term_seventh("Học kỳ 7"),
    term_eighth("Học kỳ 8"),
    term_ninth("Học kỳ 9"),
    term_tenth("Học kỳ 10"),
    term_eleventh("Học kỳ 11"),
    term_twelfth("Học kỳ 12"),
    term_thirteenth("Học kỳ 13"),
    term_fourteenth("Học kỳ 14"),
    term_fifteenth("Học kỳ 15"),
    term_sixteenth("Học kỳ 16"),
    ;

    private String value;

    ProgramTermType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
