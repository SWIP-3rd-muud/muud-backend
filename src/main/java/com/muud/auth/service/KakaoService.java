package com.muud.auth.service;

import com.muud.auth.dto.KakaoInfoResponse;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoService {
    @Value("${kakao.auth.api-key}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.auth.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.auth.redirect-url}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    //카카오 인증 페이지 요청 URI
    public String getKakaoAuth() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    //코드 -> 토큰 -> 카카오 사용자 정보
    public KakaoInfoResponse getKakaoInfo(String code) {
        try {
            Map<String, String> tokenResponse = getKakaoToken(code);
            return getUserInfoWithToken(tokenResponse.get("accessToken"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //코드 -> 인증 토큰
    public Map<String, String> getKakaoToken(String code) throws Exception {
        if (code == null) throw new ApiException(ExceptionType.INVALID_AUTHENTICATE);

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code"         , code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );
            Map<String, String> result = response.getBody();
            System.out.println(result);
            accessToken  = (String) result.get("access_token");
            refreshToken = (String) result.get("refresh_token");
        } catch (Exception e) {
            e.printStackTrace();
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
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj    = (JSONObject) jsonParser.parse(response.getBody());
        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");
        String id = String.valueOf(jsonObj.get("id"));
        String nickname = (String) profile.get("nickname");
        String email = (String) profile.get("email");

        return KakaoInfoResponse.builder()
                .socialId(id)
                .email(email)
                .nickname(nickname).build();
    }
}
