package com.websocket.chat.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.isFriendInfo;
import com.websocket.chat.dto.user_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface UserMapper {
    user_info getProfile(@Param("user_id") String user_id);
    List<friend_info> getFriendInfo(@Param("user_id") String user_id);
    void addFriend(@Param("user_id") String user_id,
                   @Param("target_id") String target_id
    );
    isFriendInfo isFriend(@Param("user_id") String user_id,
                          @Param("target_id") String target_id
    );
    List<user_info> findFriend(@Param("user_name") String user_name);
}
