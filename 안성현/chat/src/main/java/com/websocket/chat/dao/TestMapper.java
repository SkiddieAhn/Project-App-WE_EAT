package com.websocket.chat.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.websocket.chat.dto.chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface TestMapper {
    List<chat> getAllDataList();
    int getChatMaxId();
    void setChatDataList(@Param("chat_id") int chat_id,
                         @Param("chat_name") String chat_name,
                         @Param("chat_restaurant") String chat_restaurant,
                         @Param("chat_num") int chat_num,
                         @Param("chat_file_url") String chat_file_url,
                         @Param("chat_create_time") String chat_create_time
    );
    chat getChatData(@Param("chat_id") int chat_id);
}
