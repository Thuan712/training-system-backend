package com.thinkvitals.rabbitmq;

public enum ReloadActionType {
    update("Update"),
    refetch("Refetch");



    ReloadActionType(String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return this.value;
    }
}
