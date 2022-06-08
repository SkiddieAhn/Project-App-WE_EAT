package com.websocket.chat.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.service.ChatService;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatRoom {
    private int roomId;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(int roomId, String name){
        this.roomId = roomId;
    }

    // 채팅 서비스

    /*
    채팅방 입장, 대화, 퇴장은 Socket 통신
    * Request 예시
        {
          "type":"ENTER",
          "chat_id":1,
          "user_id":"user123",
          "user_name":"chulsu",
          "message":""
        }
    */

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService){
        // chatMessage 파싱
        String type= chatMessage.getType();
        int chat_id = chatMessage.getChat_id();
        String user_id = chatMessage.getUser_id();
        String user_name = chatMessage.getUser_name();
        String message= chatMessage.getMessage();

        // 채팅방 입장
        if(type.equals("ENTER")){
            // 세션 생성 및 채팅방 메시지 불러오기
            sessions.add(session);
//            List<ChatMessage> msgList = chatService.getAllChatMessage(chat_id);
//
//            // 입장한 사용자에게 메시지 보내기
//            for(int i=0; i<msgList.size(); i++){
//                ChatMessage msg= msgList.get(i);
//                chatService.sendMessage(session,msg);
//            }

            // 새로 입장 = flase, 기존 입장 = true 
            boolean status=chatService.checkUserIn(chat_id, user_id);
            if(!status)
                chatService.addChatMember(chat_id,user_id);  // chat,chatuser table에 기록
            else
                return ; // 입장했다는 메시지를 기록하지 않고 보내지도 않음
        }
        
        // 채팅방 퇴장
        else if(type.equals("OUT")){
            // 세션 삭제
            sessions.remove(session);
            // 채팅방 삭제 = true, 멤버만 삭제 = false
            boolean del = chatService.delChatMember(chat_id,user_id);
            if(del)
                return ; // 채팅방이 삭제되면 기록과 전송이 불가능함
        }

        // 뒤로가기
       else if(type.equals("BACK")){
            // 세션 삭제
            sessions.remove(session);
            return ; // 뒤로 갔다는 메시지를 기록하지 않고 보내지도 않음
        }

        // DB에 메시지 기록
        chatService.addChatMessage(type,chat_id,user_id,user_name,message);
        // 모든 사용자에게 메시지 보내기
        sendMessage(chatMessage, chatService);
    }
    
    // 메시지 전송
    public <T> void sendMessage(T message, ChatService chatService){
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session,message));
    }
}