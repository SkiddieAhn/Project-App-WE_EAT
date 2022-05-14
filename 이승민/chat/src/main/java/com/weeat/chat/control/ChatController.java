package com.weeat.chat.control;

import com.weeat.chat.DTO.ChatRoom;
import com.weeat.chat.DTO.portfolio;
import com.weeat.chat.service.ChatService;
import com.weeat.chat.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final TestService testService;
    private final ChatService chatService;
    @PostMapping
    public ChatRoom createRoom(@RequestParam String name){
        return chatService.createRoom(name);
    }

    @GetMapping
    public List<ChatRoom> findAllRoom() {
        return chatService.findAllRoom();
    }

    @GetMapping("/test")
    public List<portfolio> test(){
        return testService.getAllDataList();

    }
}
