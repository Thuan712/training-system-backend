package iuh.fit.trainingsystembackend.rabbitmq;
public enum ReloadDataType {
    none("None"),;

    ReloadDataType(String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return this.value;
    }

}
