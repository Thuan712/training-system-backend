package iuh.fit.trainingsystembackend.rabbitmq;

public enum MessagePayloadType {
    kick_out("Kick Out"),
    reload_data("Reload Data");

    private String value;

    public String getValue() {
        return this.value;
    }

    MessagePayloadType(String value) {
        this.value = value;
    }
}
