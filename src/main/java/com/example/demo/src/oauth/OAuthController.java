package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.oauth.model.OAuthLoginRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ApiResponses({
        @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 403, message = "비활성화된 유저입니다."),
        @ApiResponse(code = 404, message = "탈퇴한 유저입니다."),
        @ApiResponse(code = 400, message = "데이터베이스 연결에 실패하였습니다.")
})
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
    @ApiOperation(value = "kakao login API")
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<OAuthLoginRes> kakaoLogin() throws JsonProcessingException {
        try {
            String accessToken = kakaoService.getAccessToken();
            OAuthLoginRes oAuthLoginRes = kakaoService.kakaoLogin(accessToken);
            return new BaseResponse<>(oAuthLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
