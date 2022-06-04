package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.user_info;
import com.websocket.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

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
    public friend_info getFriendInfo(@RequestParam String friend_id) {
        return userService.getFriendInfo(friend_id);
    }

    // 친구추가
    @PostMapping("/friend/add")
    public void addFriend(@RequestParam String friend_id1,
                                @RequestParam String friend_id2) throws NoSuchAlgorithmException{
        userService.addFriend(friend_id1,friend_id2);
    }

    // 친구검색
    @GetMapping("/search")
    public user_info findFriend(@RequestParam String user_id) throws NoSuchAlgorithmException{
        return userService.findFriend(user_id);
    }
}
