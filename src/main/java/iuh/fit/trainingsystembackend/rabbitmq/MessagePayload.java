package iuh.fit.trainingsystembackend.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagePayload {
    private Long clientId;
    //#region Use for websocket and queue routing
    private MessagePayloadType type = MessagePayloadType.reload_data;
    // This is used for MessagePayloadType with type = notification
    private NotificationType notificationType = NotificationType.none;
    private ReloadDataType reloadDataType = ReloadDataType.none;
    private ReloadActionType reloadActionType =ReloadActionType.update;

    private String destination;
    private String sessionId;
    private String username;
    //#endregion

}
