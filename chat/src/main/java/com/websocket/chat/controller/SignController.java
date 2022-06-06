package com.websocket.chat.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.sign_in;
import com.websocket.chat.dto.sign_up;
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
    
    /*
    회원가입
    * Request 예시
        {
            "user_id":"minji",
            "user_pw":"minjipw",
            "user_name":"minji",
            "user_sid":"202201234",
            "user_dept":"computer"
        }
    */

    // 회원 가입
    @PostMapping("/up")
    public user_info signUp(@RequestBody sign_up obj) throws NoSuchAlgorithmException {
        // 요청받은 객체 분리
        String user_id=obj.getUser_id();
        String user_pw=obj.getUser_pw();
        String user_name=obj.getUser_name();
        String user_sid=obj.getUser_sid();
        String user_dept=obj.getUser_dept();

        // user_state 설정
        int user_state = 1;

        return signService.signUp(user_id,user_pw,user_state,user_name,user_sid,user_dept);
    }

    // 로그인
    @PostMapping("/in")
    public user_info signIn(@RequestBody sign_in obj) throws NoSuchAlgorithmException{
        // 요청받은 객체 분리
        String id=obj.getUser_id();
        String pw=obj.getUser_pw();

        return signService.signIn(id,pw);
    }

    // 로그아웃
    @GetMapping("/out")
    public int signOut(@RequestParam String user_id){
        return signService.signOut(user_id);
    }
}
