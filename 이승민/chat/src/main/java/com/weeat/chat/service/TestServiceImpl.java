package com.weeat.chat.service;

import com.weeat.chat.DTO.portfolio;
import com.weeat.chat.dao.TestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService{
    private final TestMapper testMapper;

    @Override
    public List<portfolio> getAllDataList(){
        return testMapper.getAllDataList();
    }
}
