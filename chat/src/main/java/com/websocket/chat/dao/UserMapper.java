package com.websocket.chat.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.user_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface UserMapper {
    user_info getProfileData(@Param("user_id") String user_id);
    friend_info getFriendInfo(@Param("friend_id") String friend_id);
    void addFriend(@Param("friend_id1") String friend_id1,
                   @Param("friend_id2") String friend_id2
    );
    user_info findFriend(@Param("user_id") String user_id);
}
