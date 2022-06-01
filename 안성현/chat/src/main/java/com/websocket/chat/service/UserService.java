package com.websocket.chat.service;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.UserMapper;
import com.websocket.chat.dto.friend_info;
import com.websocket.chat.dto.user;
import com.websocket.chat.dto.user_info;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

interface UserServiceIF {
    public user_info getProfile(String user_id);
    public friend_info getFriendInfo(String friend_id);
    public void addFriend(String friend_id1, String friend_id2);
    public user_info findFriend(String user_id);
}
@Slf4j
@RequiredArgsConstructor
@Service
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserService implements UserServiceIF {
    private final UserMapper userMapper;
    @Override
    public user_info getProfile(String user_id) {
        return userMapper.getProfileData(user_id);
    }
    @Override
    public friend_info getFriendInfo(String friend_id){
        return userMapper.getFriendInfo(friend_id);
    }
    @Override
    public void addFriend(String friend_id1, String friend_id2){
        userMapper.addFriend(friend_id1,friend_id2);
    }
    @Override
    public user_info findFriend(String user_id) { return userMapper.findFriend(user_id); }
}
