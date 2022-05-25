package com.websocket.chat.service;

import com.websocket.chat.dto.kakao;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private static String keyword; // 키워드
    private static int size; // 검색 결과 수
    private static String GEOCODE_URL; // 검색 주소
    private static String GEOCODE_USER_INFO="KakaoAK 4eb81c3defb0830a786e6fd84f3e3f7c"; // 카카오 API에서 발급받은 Rest API Key

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
}
