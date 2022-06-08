package com.websocket.chat.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.dao.ChatMapper;
import com.websocket.chat.dao.SignMapper;
import com.websocket.chat.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

interface ChatServiceIF {
    public List<chat> getAllDataList();
    public List<chat> getMyChatRoomList(String user_id);
    public int getChatMaxId();
    public void setChatData(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_create_time);
    public chat getChatData(int chat_id);
    public void addChatMemberList(int chat_id, List <id> userIdList);
    public boolean checkUserIn(int chat_id, String user_id);
    public void addChatMember(int chat_id, String user_id);
    public boolean delChatMember(int chat_id, String user_id);
    public void addChatMessage(String type, int chat_id, String user_id, String user_name, String message);
    public List<ChatMessage> getAllChatMessage(int chat_id);
    public void addChatMemberEnterMessage(int chat_id, List<id> userIdList);

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
    private final SignMapper signMapper;
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

    // 채팅방 생성 시각 생성
    public String makeTime(){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yy.MM.dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH");

        // 현재 날짜와 시간 기준으로 Date객체 생성
        Date now= new Date();

        // (연도,월,일) 얻기
        String nowDate=sdf1.format(now);
        // 시간 얻기
        int nowTime=Integer.parseInt(sdf2.format(now));

        // << 후처리 >>
        // 문자열 변수 선언
        String nowTimeStr;

        // 0시 (오전 12시)
        if(nowTime==0)
            nowTimeStr="12am";
        // 12시 (오후 12시)
        else if(nowTime==12)
            nowTimeStr="12pm";
        // 오전
        else if(nowTime<12)
            nowTimeStr= Integer.toString(nowTime)+"am";
        // 오후
        else
            nowTimeStr= Integer.toString(nowTime%12)+"pm";

        // 최종 문자열
        String nowDateTime=nowDate+" "+nowTimeStr;

        return nowDateTime;
    }

    // 채팅방 정보 저장
    @Override
    public void setChatData(int chat_id, String chat_name, String chat_restaurant, int chat_num, String chat_create_time){
        chatMapper.setChatData(chat_id, chat_name, chat_restaurant, chat_num, chat_create_time);
    }

    // 채팅방 멤버 정보 저장 
    @Override
    public void addChatMemberList(int chat_id, List<id> userIdList) {
        for(int i=0; i<userIdList.size(); i++){
            String user_id=userIdList.get(i).getId();
            chatMapper.addChatMember(chat_id, user_id);
        }
    }

    // 채팅방에 멤버가 입장했다는 메시지 저장
    @Override
    public void addChatMemberEnterMessage(int chat_id, List<id> userIdList){
        String type="ENTER";
        String user_id;
        String user_name;
        String message="";
        for(int i=0; i<userIdList.size(); i++){
            user_id=userIdList.get(i).getId();
            user_name=signMapper.getUserName(user_id);
            addChatMessage(type,chat_id,user_id,user_name,message);
        }
    }

    // 채팅방 객체 생성
    public ChatRoom createRoom(int chat_id) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chat_id)
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
    // * Method << 참여중인 채팅방 목록 확인 >>
    // ==================================================================
    @Override
    public List<chat> getMyChatRoomList(String user_id) {
        return chatMapper.getMyChatRoomList(user_id);
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
            size=15;
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
    // * Method << 채팅방 입장, 대화, 퇴장 >>
    // ==================================================================

    // 채팅방에 해당 유저가 있는지 확인
    @Override
    public boolean checkUserIn(int chat_id, String user_id){
        try{
            int check = chatMapper.checkUserIn(chat_id,user_id);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    // 채팅방에 유저 추가
    @Override
    public void addChatMember(int chat_id, String user_id){
        chatMapper.addChatMember(chat_id, user_id); // 유저 추가
        chatMapper.PlusChatNum(chat_id); // 인원 수 추가
    }

    // 채팅방에 유저 삭제
    @Override
    public boolean delChatMember(int chat_id, String user_id){
        chatMapper.delChatMember(chat_id, user_id);
        chatMapper.MinusChatNum(chat_id);
        // 채팅방 인원 수가 0이 되서 채팅방 삭제
        if(chatMapper.checkChatNum(chat_id)==0){
            chatMapper.delChatRoom(chat_id); // 채팅방 레코드 삭제
            ChatRooms.remove(chat_id); // 채팅방 객체 삭제
            return true;
        }
        return false;
    }


    // 모든 채팅방 메시지 반환
    @Override
    public List<ChatMessage> getAllChatMessage(int chat_id){
        return chatMapper.getAllChatMessage(chat_id);
    }

    // 채팅방에 메시지 추가
    @Override
    public void addChatMessage(String type, int chat_id, String user_id, String user_name, String message){
        chatMapper.addChatMessage(type, chat_id, user_id, user_name, message);
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
        ChatRoom room = ChatRooms.get(chat_id);;

        // 채팅방 객체가 존재하는 경우
        if(room != null)
            return room;
        // 채팅방 객체가 존재하지 않는 경우
        else{
            // chat 테이블에 chat_id에 해당하는 채팅방이 있는 경우
            try{
                int chat_num=chatMapper.checkChatNum(chat_id);
                return createRoom(chat_id); // 채팅방 객체 생성 후 반환
            }
            // chat 테이블에 chat_id에 해당하는 채팅방이 없는 경우
            catch(Exception e){
                return null;
            }
        }
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