package com.websocket.chat.dao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dto.portfolio;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public interface TestMapper {
    List<portfolio> getAllDataList();
}
