package com.websocket.chat.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatMessage {
    //메세지 타입 : 입장, 채팅, 나가기
    public enum MessageType {
        ENTER, TALK, OUT
    }

    private MessageType type;
    private int chat_id;
    private String user_name;
    private String message;
}