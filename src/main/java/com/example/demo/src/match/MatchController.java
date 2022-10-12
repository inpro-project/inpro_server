package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.match.model.GetLikerRes;
import com.example.demo.src.match.model.GetLikingRes;
import com.example.demo.src.match.model.PostUserLikeRes;
import com.example.demo.src.match.model.PostUserPassRes;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            PostUserLikeRes postUserLikeRes = matchService.createUserLike(likerIdx, likingIdx);
            return new BaseResponse<>(postUserLikeRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 좋아요 취소 API
     * [PATCH] /app/user-likes/:likingIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 좋아요 취소 API", notes = "성공 시 result로 '좋아요가 취소되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 327, message = "기존에 좋아요를 누르지 않은 유저입니다."),
            @ApiResponse(code = 410, message = "유저 좋아요 취소에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/user-likes/{likingIdx}")
    public BaseResponse<String> deleteUserLike(@PathVariable("likingIdx") int likingIdx) {
        try {
            int likerIdx = jwtService.getUserIdx();

            matchService.deleteUserLike(likerIdx, likingIdx);
            String result = "좋아요가 취소되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 넘기기 API
     * [POST] /app/user-passes/:passingIdx
     * @return BaseResponse<PostUserPassRes>
     */
    @ApiOperation(value = "유저 넘기기 API")
    @ApiResponses({
            @ApiResponse(code = 328, message = "이미 넘기기를 누른 유저입니다."),
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 411, message = "유저 넘기기에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("/user-passes/{passingIdx}")
    public BaseResponse<PostUserPassRes> createUserPass(@PathVariable("passingIdx") int passingIdx) {
        try {
            int passerIdx = jwtService.getUserIdx();

            PostUserPassRes postUserPassRes = matchService.createUserPass(passerIdx, passingIdx);
            return new BaseResponse<>(postUserPassRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내가 보낸 좋아요 조회 API
     * [GET] /app/likings
     * @return BaseResponse<List<GetLikingsRes>>
     */
    @ApiOperation(value = "내가 보낸 좋아요 조회 API")
    @ResponseBody
    @GetMapping("/likings")
    public BaseResponse<List<GetLikingRes>> getLikings(){
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetLikingRes> getLikingResList = matchProvider.getLikings(userIdx);
            return new BaseResponse<>(getLikingResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 나에게 보낸 좋아요 조회 API
     * [GET] /app/likers
     * @return BaseResponse<List<GetLikerRes>>
     */
    @ApiOperation(value = "나에게 보낸 좋아요 조회 API")
    @ResponseBody
    @GetMapping("/likers")
    public BaseResponse<List<GetLikerRes>> getLikers(){
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetLikerRes> getLikerResList = matchProvider.getLikers(userIdx);
            return new BaseResponse<>(getLikerResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
