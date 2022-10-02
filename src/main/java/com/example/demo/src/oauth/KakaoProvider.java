package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.OAuthLoginRes;
import com.example.demo.src.oauth.model.User;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoProvider {

    private final KakaoDao kakaoDao;
    private final JwtService jwtService;

    public int checkEmail(String email) throws BaseException {
        try {
            return kakaoDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public OAuthLoginRes logIn(String email) throws BaseException {
        // 이메일 유효성 검사
        if(kakaoDao.checkEmail(email) == 0){
            throw new BaseException(FAILED_TO_LOGIN);
        }

        User user = kakaoDao.getUser(email);

        // 유저 상태 유효성 검사
        if(user.getStatus().equals("inactive")) {
            throw new BaseException(INACTIVE_USER);
        }
        if(user.getStatus().equals("deleted")) {
            throw new BaseException(DELETED_USER);
        }

        int userIdx = user.getUserIdx();
        String jwt = jwtService.createJwt(userIdx);
        return new OAuthLoginRes(userIdx, jwt);
    }


}
