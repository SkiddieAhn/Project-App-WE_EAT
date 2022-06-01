package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.dao.ChatMapper;
import com.websocket.chat.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.FileWriter;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

interface ChatServiceIF {
    public List<chat> getAllDataList();
    public int getChatMaxId();
    public void setChatData(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time);
    public chat getChatData(int chat_id);
    public void addUserToChat(int chat_id, List <userId> userIdList);
}

@Slf4j
@RequiredArgsConstructor
@Service
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChatService implements ChatServiceIF{

    // ==================================================================
    // * Field << 카카오 API 필드 >>
    // ==================================================================
    private static String keyword; // 키워드
    private static int size; // 검색 결과 수
    private static String GEOCODE_URL; // 검색 주소
    private static String GEOCODE_USER_INFO="KakaoAK 4eb81c3defb0830a786e6fd84f3e3f7c"; // 카카오 API에서 발급받은 Rest API Key

    // ==================================================================
    // * Field << 서버 필드 >>
    // ==================================================================
    private final ObjectMapper objectMapper;
    private final ChatMapper chatMapper;
    private Map<Integer, ChatRoom> ChatRooms;


    // ==================================================================
    // * Method << 채팅방 생성 >>
    // ==================================================================
    
    // 채팅방 ID 중 MAX값 찾기
    @Override
    public int getChatMaxId(){
        // 채팅방 목록이 존재하는 경우
        try{
            int id = chatMapper.getChatMaxId();
            return id;
        }
        // 채팅방 목록이 존재하지 않는 경우
        catch(Exception e){
            return 0;
        }
    }

    //채팅방 파일 생성
    public void makeFile(int chat_id) {
        String fileName = Integer.toString(chat_id) + ".txt";
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter("src/main/resources/chatfile/" + file, true);
            fw.write("파일 생성");
            fw.flush();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 채팅방 정보 저장
    @Override
    public void setChatData(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_file_url, String chat_create_time){
        chatMapper.setChatData(chat_id, chat_name, chat_restaurant, chat_num, chat_file_url, chat_create_time);
    }

    // 채팅방 멤버 정보 저장 
    @Override
    public void addUserToChat(int chat_id, List<userId> userIdList) {
        for(int i=0; i<userIdList.size(); i++){
            String user_id=userIdList.get(i).getUser_id();
            chatMapper.addUserToChat(chat_id, user_id);
        }
    }

    // 채팅방 객체 생성
    public ChatRoom createRoom(int chat_id, String chat_name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chat_id)
                .name(chat_name)
                .build();
        ChatRooms.put(chat_id,chatRoom);
        return chatRoom;
    }

    // ==================================================================
    // * Method << 채팅방 목록 확인 >>
    // ==================================================================
    @Override
    public List<chat> getAllDataList() {
        return chatMapper.getAllDataList();
    }

    // ==================================================================
    // * Method << 특정 채팅방 정보 확인 >>
    // ==================================================================
    @Override
    public chat getChatData(int chat_id) {
        return chatMapper.getChatData(chat_id);
    }

    // ==================================================================
    // * Method << 음식점 검색 >>
    // ==================================================================
    public List<kakao> search(String keyword){
        URL obj; // kakao api url
        List<kakao> list=new ArrayList<kakao>(); // 리스트: ((음식점 이름, 음식점 주소),...)

        try{
            // Kakao API - 키워드로 지역 검색
            keyword = URLEncoder.encode("역곡"+keyword, "UTF-8");
            size=10;
            GEOCODE_URL=String.format("http://dapi.kakao.com/v2/local/search/keyword.json?query=%s&size=%d",keyword,size);
            System.out.println("검색 URL: "+GEOCODE_URL);

            obj = new URL(GEOCODE_URL);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization",GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            String jsonString=response.toString();

            // Json 문자열 파싱 -> List<kakao>에 저장
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONArray jsonArray = (JSONArray) jsonObject.get("documents");
            try {
                jsonObject = (JSONObject) jsonParser.parse(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject objectInArray = (JSONObject) jsonArray.get(i);
                    String place_name=(String) objectInArray.get("place_name");
                    String address_name=(String) objectInArray.get("address_name");
                    kakao k=new kakao(place_name,address_name);
                    list.add(k);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }


    // ==================================================================
    // * Method << 기타 메소드 >>
    // ==================================================================

    // Map 자료구조 초기화
    @PostConstruct
    private void init() {
        ChatRooms = new LinkedHashMap<>();
    }

    // 특정 채팅방 객체 반환
    public ChatRoom findRoomById(int chat_id){
        return ChatRooms.get(chat_id);
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