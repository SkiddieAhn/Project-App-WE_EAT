package com.weeat.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weeat.chat.DTO.ChatMessage;
import com.weeat.chat.DTO.ChatRoom;
import com.weeat.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler  extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}",payload);
        //TextMessage textMessage = new TextMessage("Welcome chat server~");
        //session.sendMessage(textMessage);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, chatService);
    }
}
