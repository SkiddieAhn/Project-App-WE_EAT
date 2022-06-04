package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.*;
import com.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatController {

    private final ChatService chatService;

    /*
    채팅방 입장, 대화, 퇴장은 Socket 통신
    * Request 예시
        {
          "type":"ENTER",
          "chat_id":1,
          "user_name":"chulsu",
          "message":""
        }
    */

    /*
    채팅방 생성
    * Request 예시
        {
            "chat_name":"chatroom",
            "chat_restaurant":"chicken",
            "userIdList":[
                {"user_id":"minji"},
                {"user_id":"chulsu"},
                {"user_id":"gihyun"}
            ]
        }
    */

    // 채팅방 생성
    @PostMapping("/create")
    public chat create(@RequestBody create_chat obj)
    {
        // 요청받은 객체 분리
        String chat_name=obj.getChat_name();
        String chat_restaurant=obj.getChat_restaurant();
        List<userId> userIdList=obj.getUserIdList();

        // (DB) chat table에 저장

        // 1. max 값으로 id 선정
        int chat_id=chatService.getChatMaxId()+1;

        // 2. chat_num 초기값은 userIdList의 크기
        int chat_num=userIdList.size();

        // 3. chat_create_time 초기값은 현재 시각
        String time=chatService.makeTime();
        String chat_create_time=time;

        // 4. 채팅방 정보 저장
        chatService.setChatData(chat_id, chat_name, chat_restaurant, chat_num, chat_create_time);

        // (DB) chatuser table에 저장
        chatService.addUserToChat(chat_id,userIdList);

        // 채팅방 객체 생성
        chatService.createRoom(chat_id, chat_name);

        // 삽입한 채팅방 정보 반환
        return chatService.getChatData(chat_id);
    }

    // 채팅방 목록 확인
    @GetMapping("/search")
    public List<chat> search(){
        return chatService.getAllDataList();
    }

    // 특정 채팅방 정보 확인
    @GetMapping("/room")
    public chat room(@RequestParam int chat_id){return chatService.getChatData(chat_id);}

    // 음식점 검색
    @GetMapping("/food")
    public List<kakao> search(@RequestParam String keyword){
        return chatService.search(keyword);
    }
}
