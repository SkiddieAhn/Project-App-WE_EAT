package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.UserMapper;
import com.websocket.chat.dto.user_info;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

interface UserServiceIF{
    public user_info getProfile(String user_id);
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
        userMapper.getProfile(user_id);
    }
}
