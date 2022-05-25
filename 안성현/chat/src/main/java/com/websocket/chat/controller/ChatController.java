package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.ChatRoom;
import com.websocket.chat.dto.ChatRoomInfor;
import com.websocket.chat.dto.chat;
import com.websocket.chat.service.ChatService;
import com.websocket.chat.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatController {

    private final TestService testService;
    private final ChatService chatService;

    /*
    채팅방 입장, 대화, 퇴장은 Socket 통신
    * Request 예시
        {
          "type":"ENTER",
          "roomId":"9e648d2d-5e2e-42b3-82fc-b8bef8111cbe",
          "sender":"happydaddy",
          "message":""
        }
    */
    
    // 채팅방 생성
    @PostMapping
    public ChatRoom createRoom(@RequestParam String name){
        return chatService.createRoom(name);
    }

    // 채팅방 목록 확인
    @GetMapping
    public List<ChatRoomInfor> findAllRoom() {
        return chatService.findAllRoom();
    }

    // 특정 채팅방 정보 확인
    @PostMapping("/room")
    public ChatRoomInfor findRoomWithId(@RequestParam String id) {
        return chatService.findRoomWithId(id);
    }

    // 채팅방 목록 확인 (DB)
    @GetMapping("/list")
    public List<chat> list(){
        return testService.getAllDataList();
    }

    // 채팅방 생성 및 저장 (DB)
    @PostMapping("/make")
    public chat make(@RequestParam String chat_name,
                     @RequestParam String chat_restaurant,
                     @RequestParam int chat_num,
                     @RequestParam String chat_file_url,
                     @RequestParam String chat_create_time)
    {
        // max 값으로 id 선정
        int chat_id=testService.getChatMaxId()+1;
        // insert 시도
        testService.setChatDataList(chat_id, chat_name, chat_restaurant, chat_num, chat_file_url, chat_create_time);
        // 삽입한 채팅방 정보 반환
        return testService.getChatData(chat_id);
    }
}
