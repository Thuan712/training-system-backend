package iuh.fit.trainingsystembackend.rabbitmq;
public enum NotificationType {
        none("None"),
      ;
        private String value;
        private NotificationType(String value) {
            this.value = value;
        }
        public String getValue() {return value;}
    }
