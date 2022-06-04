package com.websocket.chat.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.ChatMessage;
import com.websocket.chat.dto.chat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface ChatMapper {
    List<chat> getAllDataList();
    int getChatMaxId();
    void setChatData(@Param("chat_id") int chat_id,
                     @Param("chat_name") String chat_name,
                     @Param("chat_restaurant") String chat_restaurant,
                     @Param("chat_num") int chat_num,
                     @Param("chat_create_time") String chat_create_time
    );
    chat getChatData(@Param("chat_id") int chat_id);
    void addChatMember(@Param("chat_id") int chat_id,
                       @Param("user_id") String user_id);
    int checkUserIn(@Param("chat_id") int chat_id,
                    @Param("user_id") String user_id);

    List<ChatMessage> getAllChatMessage(@Param("chat_id") int chat_id);
    void addChatMessage(@Param("type") String type,
                        @Param("chat_id") int chat_id,
                        @Param("user_id") String user_id,
                        @Param("user_name") String user_name,
                        @Param("message") String message
    );

    void PlusChatNum(@Param("chat_id") int chat_id);
}
