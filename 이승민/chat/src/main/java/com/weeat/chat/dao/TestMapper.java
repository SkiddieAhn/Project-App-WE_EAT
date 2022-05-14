package com.weeat.chat.dao;

import com.weeat.chat.DTO.portfolio;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {
    List<portfolio> getAllDataList();
}