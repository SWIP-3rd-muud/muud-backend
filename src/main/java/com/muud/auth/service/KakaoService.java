package com.muud.auth.service;

import com.muud.auth.domain.dto.KakaoInfoResponse;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoService {

    private String KAKAO_CLIENT_ID;

    private String KAKAO_CLIENT_SECRET;

    private String KAKAO_REDIRECT_URL;

    @Autowired
    public KakaoService(@Value("${kakao.auth.api-key}") String KAKAO_CLIENT_ID, @Value("${kakao.auth.client-secret}") String KAKAO_CLIENT_SECRET,  @Value("${kakao.auth.redirect-url}") String KAKAO_REDIRECT_URL) {
        this.KAKAO_CLIENT_ID = KAKAO_CLIENT_ID;
        this.KAKAO_CLIENT_SECRET = KAKAO_CLIENT_SECRET;
        this.KAKAO_REDIRECT_URL = KAKAO_REDIRECT_URL;
    }

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    //코드 -> 토큰 -> 카카오 사용자 정보
    public KakaoInfoResponse getKakaoInfo(String code) {
        if(code == null){
            throw new ApiException(ExceptionType.INVALID_INPUT_VALUE);
        }
        try {
            Map<String, String> tokenResponse = getKakaoToken(code);
            return getUserInfoWithToken(tokenResponse.get("accessToken"));
        } catch (Exception e) {
            throw new ApiException(ExceptionType.BAD_REQUEST, e.getMessage());
        }
    }

    //코드 -> 인증 토큰
    public Map<String, String> getKakaoToken(String code){
        String accessToken = "";
        String refreshToken = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("Accept", "application/json");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id"    , KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code", code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    KAKAO_AUTH_URI,
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            Map<String, String> result = response.getBody();
            accessToken  = result.get("access_token");
            System.out.println(accessToken);
            refreshToken = result.get("refresh_token");
        } catch (Exception e) {
            throw e;
        }
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    private KakaoInfoResponse getUserInfoWithToken(String accessToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 파싱
        //추후 협의 후 필요한 정보 추가
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");
        String id = String.valueOf(jsonObj.get("id"));
        String nickname = (String) profile.get("nickname");
        String email = (String) account.get("email");

        return KakaoInfoResponse.of(id, email, nickname);
    }
}
