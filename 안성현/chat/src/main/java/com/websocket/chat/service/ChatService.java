package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.dto.ChatRoom;
import com.websocket.chat.dto.ChatRoomInfor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatService {

    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> ChatRooms;
    private Map<String, ChatRoomInfor> ChatRoomsInfor;

    // Map 자료구조 초기화
    @PostConstruct
    private void init() {
        ChatRooms = new LinkedHashMap<>();
        ChatRoomsInfor = new LinkedHashMap<>();
    }

    // 모든 채팅방 정보 반환
    public List<ChatRoomInfor> findAllRoom() {
        return new ArrayList<>(ChatRoomsInfor.values());
    }

    // 특정 채팅방 정보 반환
    public ChatRoomInfor findRoomWithId(String roomId) {
        return ChatRoomsInfor.get(roomId);
    }

    // 특정 채팅방 객체 반환
    public ChatRoom findRoomById(String roomId) {
        return ChatRooms.get(roomId);
    }

    // 채팅방 및 채팅방 정보 객체 생성
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        // 채팅방 정보 객체 생성 및 저장
        ChatRoomInfor infor = ChatRoomInfor.builder()
                .roomId(randomId)
                .name(name)
                .number(0)
                .build();
        ChatRoomsInfor.put(randomId,infor);
        // 채팅방 객체 생성 및 저장
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .infor(infor)
                .build();
        ChatRooms.put(randomId,chatRoom);
        return chatRoom;
    }

    // 메시지 전송
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}