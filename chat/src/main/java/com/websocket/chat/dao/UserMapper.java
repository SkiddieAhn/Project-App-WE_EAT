package com.websocket.chat.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface UserMapper {
}
