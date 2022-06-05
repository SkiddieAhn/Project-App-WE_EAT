package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.UserMapper;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.user_info;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

interface UserServiceIF{
    public user_info getProfile(String user_id);
    public List<friend_info> getFriendInfo(String user_id);
    public int addFriend(String user_id, String target_id);
    public List<user_info> findFriend(String user_name);
}
@Slf4j
@RequiredArgsConstructor
@Service
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserService implements UserServiceIF{
    private final UserMapper userMapper;
    @Override
    public user_info getProfile(String user_id) {
        return userMapper.getProfile(user_id);
    }
    @Override
    public List<friend_info> getFriendInfo(String user_id){
        return userMapper.getFriendInfo(user_id);
    }
    @Override
    public int addFriend(String user_id, String target_id){
        return userMapper.addFriend(user_id,target_id);
    }
    @Override
    public List<user_info> findFriend(String user_name){
        return userMapper.findFriend(user_name);
    }
}
