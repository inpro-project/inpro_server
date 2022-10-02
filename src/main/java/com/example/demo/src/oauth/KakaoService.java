package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.KakaoUser;
import com.example.demo.src.oauth.model.OAuthLoginRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
@RequiredArgsConstructor
@Transactional
public class KakaoService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    private final KakaoProvider kakaoProvider;

    private final KakaoDao kakaoDao;

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    private final String GRANT_TYPE = "authorization_code";
    private final String REDIRECT_URI = "https://prod.inpro-server.shop/oauth/kakao";

    public OAuthLoginRes kakaoLogin(String code) throws JsonProcessingException, BaseException {

        // 1. 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2. 액세스 토큰으로 카카오 API 호출
        KakaoUser kakaoUser = getKakaoUser(accessToken);

        // 3. 존재하지 않는 회원이면 회원 가입 진행
        if(kakaoProvider.checkEmail(kakaoUser.getEmail()) == 0){
            createUser(kakaoUser);
        }

        // 4. 로그인
        OAuthLoginRes oAuthLoginRes = kakaoProvider.logIn(kakaoUser.getEmail());
        return oAuthLoginRes;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // http header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", CONTENT_TYPE);

        // http body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        // http 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // http 응답 (json) -> 액세스 토큰 parsing
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUser getKakaoUser(String accessToken) throws JsonProcessingException {
        // http header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", CONTENT_TYPE);

        // http 요청
        HttpEntity<MultiValueMap<String, String>> kakaoUserRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserRequest,
                String.class
        );

        // response body 정보 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickName = jsonNode.get("properties").get("nickname").asText();
        String userImgUrl = jsonNode.get("kakao_account").get("profile").get("thumbnail_image_url").asText();
        String age_range = jsonNode.get("kakao_account").get("age_range").asText();
        String gender = jsonNode.get("kakao_account").get("gender").asText();

        return new KakaoUser(email, nickName, userImgUrl, age_range, gender);
    }

    public void createUser(KakaoUser kakaoUser) throws BaseException {
        try {
            kakaoDao.createUser(kakaoUser);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
