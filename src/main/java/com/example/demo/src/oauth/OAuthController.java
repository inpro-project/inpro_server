package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.oauth.model.OAuthLoginRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final KakaoService kakaoService;

    /**
     * kakao login API
     * [GET] /oauth/kakao
     * @return BaseResponse<OAuthLoginRes>
     */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<OAuthLoginRes> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        try {
            OAuthLoginRes oAuthLoginRes = kakaoService.kakaoLogin(code);
            return new BaseResponse<>(oAuthLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
