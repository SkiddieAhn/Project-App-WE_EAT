package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.TestMapper;
import com.websocket.chat.dto.chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

interface TestServiceIF {
    public List<chat> getAllDataList();
    public int setChatDataList(String chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time);
    public chat lastRecord();
}

@Service
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TestService implements TestServiceIF {
    private final TestMapper testMapper;

    @Override
    public List<chat> getAllDataList() {
        return testMapper.getAllDataList();
    }

    @Override
    public int setChatDataList(String chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time){
        try{
            testMapper.setChatDataList(chat_id, chat_name, chat_restaurant, chat_num, chat_file_url, chat_create_time);
            return 1;
        }
        catch(Exception e){
            return 0;
        }
    }

    @Override
    public chat lastRecord(){
        return testMapper.lastRecord();
    }
}
