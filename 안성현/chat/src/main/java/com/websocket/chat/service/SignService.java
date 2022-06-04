package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.SignMapper;
import com.websocket.chat.dto.user_info;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SHA256 {
    public String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
interface SignServiceIF {
    public user_info signUp(String user_id,String user_pw,int user_state,String user_name,String user_sid,String user_dept) throws NoSuchAlgorithmException;
    public user_info signIn(String id, String pw) throws NoSuchAlgorithmException;
    public void signOut(String id);
}

@Service
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SignService implements SignServiceIF{
    // ==================================================================
    // * Field << 서버 필드 >>
    // ==================================================================
    private final SignMapper signMapper;


    // ==================================================================
    // * Method << 회원가입 >>
    // ==================================================================
    @Override
    public user_info signUp(String user_id,String user_pw,int user_state,String user_name,String user_sid,String user_dept) throws NoSuchAlgorithmException {
        // 유저 정보 객체
        user_info ui = new user_info();

        //SHA256으로 비밀번호 해싱
        SHA256 sha256 = new SHA256();
        String sha_pw = sha256.encrypt(user_pw);

        // 최대 해싱 길이는 30자
        if(sha_pw.length()>30)
            sha_pw=sha_pw.substring(0,29);

        try{
            signMapper.signUp(user_id,sha_pw,user_state,user_name,user_dept,user_sid);
            // id로 user_info 검색 및 반환
            ui = signMapper.getUserInfo(user_id);
            return ui;
       }
       catch(Exception e){
           // 빈 객체 반환
           return ui;
       }
    }
    
    // ==================================================================
    // * Method << 로그인 >>
    // ==================================================================
    @Override
    public user_info signIn(String id, String pw) throws NoSuchAlgorithmException {
        // 변수 선언 및 초기화
        String user_pw = null;
        user_info ui = new user_info();

        try{
            // id로 user_pw 검색
            user_pw = signMapper.getUserPw(id);

            //SHA256으로 비밀번호 해싱
            SHA256 sha256 = new SHA256();
            String sha_pw = sha256.encrypt(pw);

            // 최대 해싱 길이는 30자
            if(sha_pw.length()>30)
                sha_pw=sha_pw.substring(0,29);

            // 비밀번호 해싱 값 비교
            if(user_pw.equals(sha_pw)){
                // 유저 상태 변경
                signMapper.setUserState_one(id);

                // id로 user_info 검색
                ui = signMapper.getUserInfo(id);
            }
            
            // 유저 정보 반환
            return ui;
        }
        catch(Exception e){
            // 실패하면 null이 담긴 user 객체 반환
            return ui;
        }
    }
    
    // ==================================================================
    // * Method << 로그아웃 >>
    // ==================================================================
    @Override
    public void signOut(String id){
        signMapper.setUserState_zero(id);
    }

}
