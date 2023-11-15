package com.thinkvitals.rabbitmq;
public enum NotificationType {
        none("None"),
        seizure("Seizure"),
        fall("Fall"),
        news("News"),
        tas("Tas"),
        battery("Battery"),
        emergency("Emergency");
        private String value;
        private NotificationType(String value) {
            this.value = value;
        }
        public String getValue() {return value;}
    }
