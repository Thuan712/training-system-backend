package iuh.fit.trainingsystembackend.rabbitmq;

public enum MessagePayloadType {
    notification("Notification"),
    kick_out("Kick Out"),
    reload_data("Reload Data"),
    chat_message("Chat Message"),
    hcp_role_change("HCP Role Change"),
    upload("Upload");

    private String value;

    public String getValue() {
        return this.value;
    }

    MessagePayloadType(String value) {
        this.value = value;
    }
}
