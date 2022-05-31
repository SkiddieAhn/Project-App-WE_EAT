package com.websocket.chat.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomInfor{
    private String roomId;
    private String name;
    private int number;

    @Builder
    public ChatRoomInfor(String roomId, String name,int number){
        this.roomId = roomId;
        this.name=name;
        this.number=0;
    }
}
