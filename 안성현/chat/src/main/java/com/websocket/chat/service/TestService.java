package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.websocket.chat.dao.TestMapper;
import com.websocket.chat.dto.portfolio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

interface TestServiceIF {
    public List<portfolio> getAllDataList();
}

@Service
@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TestService implements TestServiceIF {
    private final TestMapper testMapper;

    @Override
    public List<portfolio> getAllDataList() {
        return testMapper.getAllDataList();
    }
}
