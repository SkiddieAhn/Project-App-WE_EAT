package com.websocket.chat.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.service.ChatService;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatRoom {
    private String roomId;
    private ChatRoomInfor infor;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, ChatRoomInfor infor){
        this.roomId = roomId;
        this.infor=infor;
    }
    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            infor.setNumber(infor.getNumber()+1);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }
        else if(chatMessage.getType().equals(ChatMessage.MessageType.OUT)){
            sessions.remove(session);
            infor.setNumber(infor.getNumber()-1);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하셨습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session,message));
    }
}