package com.weeat.chat.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    //메세지 타입 : 입장, 채팅, 나가기
    public enum MessageType {
        ENTER, TALK, OUT
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
