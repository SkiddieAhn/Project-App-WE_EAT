package com.websocket.chat.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class chat {
    private String chat_id;
    private String chat_name;
    private String chat_restaurant;
    private int chat_num;
    private String chat_file_url;
    private String chat_create_time;
}