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
    private int roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(int roomId, String name){
        this.roomId = roomId;
        this.name=name;
    }

    // 채팅 서비스 
    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        // 채팅방 입장
        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)){
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }
        // 채팅방 퇴장
        else if(chatMessage.getType().equals(ChatMessage.MessageType.OUT)){
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하셨습니다.");
        }
        // 입장, 퇴장, 대화
        sendMessage(chatMessage, chatService);
    }
    
    // 메시지 전송
    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session,message));
    }
}