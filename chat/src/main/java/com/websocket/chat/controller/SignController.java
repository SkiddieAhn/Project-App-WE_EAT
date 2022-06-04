package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.user_info;
import com.websocket.chat.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sign")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SignController {
    private final SignService signService;

    // 회원 가입
    @PostMapping("/up")
    public int signUp(@RequestParam String user_id,
                      @RequestParam String user_pw,
                      @RequestParam String user_name,
                      @RequestParam String user_sid,
                      @RequestParam String user_dept) throws NoSuchAlgorithmException {
        int user_state = 1;
        // 회원가입 실패 -> 0, 회원가입 성공 -> 1
        return signService.signUp(user_id,user_pw,user_state,user_name,user_sid,user_dept);
    }

    // 로그인
    @PostMapping("/in")
    public user_info signIn(@RequestParam String id,
                            @RequestParam String pw) throws NoSuchAlgorithmException{
        return signService.signIn(id,pw);
    }

    // 로그아웃
    @GetMapping("/out")
    public void signOut(@RequestParam String id){
        signService.signOut(id);
    }
}
