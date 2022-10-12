package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.match.model.PostUserLikeRes;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USERIDX;
import static com.example.demo.config.BaseResponseStatus.USERLIKE_INVALID_LIKINGIDX;

@Slf4j
@ApiResponses({
        @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
        @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다."),
        @ApiResponse(code = 400, message = "데이터베이스 연결에 실패하였습니다.")
})
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class MatchController {

    private final MatchProvider matchProvider;

    private final MatchService matchService;

    private final JwtService jwtService;

    /**
     * 유저 좋아요 API
     * [POST] /app/user-likes/:likingIdx
     * @return BaseResponse<PostUserLikeRes>
     */
    @ApiOperation(value = "유저 좋아요 API")
    @ApiResponses({
            @ApiResponse(code = 325, message = "이미 좋아요를 누른 유저입니다."),
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 409, message = "유저 좋아요에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("/user-likes/{likingIdx}")
    public BaseResponse<PostUserLikeRes> createUserLike(@PathVariable("likingIdx") int likingIdx) {
        try {
            int likerIdx = jwtService.getUserIdx();

            // likingIdx 유효성 검사
            if(matchProvider.checkUserIdx(likingIdx) == 0){
                return new BaseResponse<>(INVALID_USERIDX);
            }

            // 중복 좋아요 유효성 검사
            if(matchProvider.checkPreUserLike(likerIdx, likingIdx) == 1){
                return new BaseResponse<>(USERLIKE_INVALID_LIKINGIDX);
            }

            // 이전에 비활성화나 좋아요 취소를 했던 유저의 경우
            int userLikeIdx = matchProvider.checkUserLikeHist(likerIdx, likingIdx);
            if(userLikeIdx != 0){
                // active 업데이트
                PostUserLikeRes postUserLikeRes = matchService.updateUserLike(userLikeIdx);
                return new BaseResponse<>(postUserLikeRes);
            }

            PostUserLikeRes postUserLikeRes = matchService.createUserLike(likerIdx, likingIdx);
            return new BaseResponse<>(postUserLikeRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
