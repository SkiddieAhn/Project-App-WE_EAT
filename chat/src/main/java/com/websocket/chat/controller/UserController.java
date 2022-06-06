package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.isFriendInfo;
import com.websocket.chat.dto.user_info;
import com.websocket.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserController {
    private final UserService userService;

    // 프로필 확인
    @GetMapping("/profile")
    public user_info getProfile(@RequestParam String user_id) {
        return userService.getProfile(user_id);
    }

    // 친구목록확인
    @GetMapping("/friend")
    public List<friend_info> getFriendInfo(@RequestParam String user_id) {
        return userService.getFriendInfo(user_id);
    }

    // 친구추가
    @PostMapping("/friend/add")
    public int addFriend(@RequestBody isFriendInfo isfriendinfo){
        String user_id = isfriendinfo.getUser_id();
        String target_id = isfriendinfo.getTarget_id();
        //친구추가
        return userService.addFriend(user_id,target_id);
    }

    // 친구검색
    @GetMapping("/search")
    public List<user_info> findFriend(@RequestParam String user_name) throws NoSuchAlgorithmException{
        return userService.findFriend(user_name);
    }
}
