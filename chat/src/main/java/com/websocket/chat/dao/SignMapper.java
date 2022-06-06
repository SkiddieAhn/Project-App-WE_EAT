package com.websocket.chat.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.chat;
import com.websocket.chat.dto.user_info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface SignMapper {
    void signUp(@Param("user_id") String user_id,
                @Param("user_pw") String user_pw,
                @Param("user_state") int user_state,
                @Param("user_name") String user_name,
                @Param("user_dept") String user_dept,
                @Param("user_sid") String user_sid
                );
    String getUserName(@Param("id") String id);
    String getUserPw(@Param("id") String id);
    int getUserState(@Param("id") String id);
    user_info getUserInfo(@Param("id") String id);
    void setUserState_one(@Param("id") String id);
    void setUserState_zero(@Param("id") String id);
}
