package com.thinkvitals.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagePayload {
    private Long clientId;
    //#region Use for websocket and queue routing
    private MessagePayloadType type = MessagePayloadType.chat_message;
    // This is used for MessagePayloadType with type = notification
    private NotificationType notificationType = NotificationType.none;
    private ReloadDataType reloadDataType = ReloadDataType.none;
    private ReloadActionType reloadActionType =ReloadActionType.update;

    private String destination;
    private String sessionId;
    private String username;
    //#endregion
    //#region Message
    private Long id;
    private Long participantId;
    private Long roomId;
    private String data;
    private String fullName;
    private String avatar;
    private Long userId;
    //#region SenderId as UserId for bean data
    private Long senderId;
    //#endregion
    private Date createdAt = new Date();
}
