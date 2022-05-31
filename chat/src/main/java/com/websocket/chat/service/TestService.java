package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.TestMapper;
import com.websocket.chat.dto.chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

interface TestServiceIF {
    public List<chat> getAllDataList();
    public int getChatMaxId();
    public void setChatDataList(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time);
    public chat getChatData(int chat_id);
}

@Service
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TestService implements TestServiceIF {
    private final TestMapper testMapper;

    // 모든 채팅방 목록 확인
    @Override
    public List<chat> getAllDataList() {
        return testMapper.getAllDataList();
    }

    // 채팅방 ID 중 MAX값 찾기
    @Override
    public int getChatMaxId(){
        // 채팅방 목록이 존재하는 경우
        try{
            int id = testMapper.getChatMaxId();
            return id;
        }
        // 채팅방 목록이 존재하지 않는 경우
        catch(Exception e){
            return 0;
        }
    }

    // 채팅방 정보 저장
    @Override
    public void setChatDataList(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time){
        testMapper.setChatDataList(chat_id, chat_name, chat_restaurant, chat_num, chat_file_url, chat_create_time);
    }

    // 채팅방 ID로 채팅방 정보 확인
    @Override
    public chat getChatData(int chat_id) {
        return testMapper.getChatData(chat_id);
    }
}
