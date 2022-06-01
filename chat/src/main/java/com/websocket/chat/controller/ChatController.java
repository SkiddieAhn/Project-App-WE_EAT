package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.*;
import com.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
          "roomId":"9e648d2d-5e2e-42b3-82fc-b8bef8111cbe",
          "sender":"happydaddy",
          "message":""
        }
    */

    // 채팅방 생성
    @PostMapping("/create")
    public chat create(@RequestParam String chat_name,
                       @RequestParam String chat_restaurant,
                       @RequestParam List<userId> userIdList)
    {
        // (DB) chat table에 저장

        // 1. max 값으로 id 선정
        int chat_id=chatService.getChatMaxId()+1;

        // 2. chat_num 초기값은 0
        int chat_num=0;

        // 3. 채팅방 파일 생성 및 chat_file_url 설정
        chatService.makeFile(chat_id);
        String chat_file_url="/file";

        // 4. chat_create_time 초기값은 현재 시각
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm");
        Date now= new Date();
        String nowTime=sdf.format(now);
        String chat_create_time=nowTime;

        // 5. 채팅방 정보 저장
        chatService.setChatData(chat_id, chat_name, chat_restaurant, chat_num, chat_file_url, chat_create_time);

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
