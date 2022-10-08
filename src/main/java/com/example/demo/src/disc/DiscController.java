package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.disc.model.*;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@ApiResponses({
        @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 2001, message = "JWT를 입력해주세요."),
        @ApiResponse(code = 2002, message = "유효하지 않은 JWT입니다."),
        @ApiResponse(code = 2003, message = "권한이 없는 유저의 접근입니다."),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
})
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class DiscController {

   private final DiscProvider discProvider;
   private final DiscService discService;
   private final JwtService jwtService;

    /**
     * 업무 성향 테스트지 조회 API
     * [GET] /app/discs
     * @return BaseResponse<List<GetDiscTestRes>>
     */
    @ApiOperation(value = "업무 성향 테스트지 조회 API")
    @ResponseBody
    @GetMapping("/discs")
    public BaseResponse<List<GetDiscTestRes>> getDiscTest(){
        try {
            List<GetDiscTestRes> getDiscTestRes = discProvider.getDiscTest();
            return new BaseResponse<>(getDiscTestRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 업무 성향 테스트 API
     * [POST] /app/user-discs
     * @return BaseResponse<PostUserDiscRes>
     */
    @ApiOperation(value = "유저 업무 성향 테스트 API")
    @ApiResponses({
            @ApiResponse(code = 2004, message = "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 2005, message = "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 2006, message = "업무 유형 name을 입력해주세요"),
            @ApiResponse(code = 2007, message = "올바르지 않은 discFeatureIdx입니다.")
    })
    @ResponseBody
    @PostMapping("/user-discs")
    public BaseResponse<PostUserDiscRes> createUserDisc(@RequestBody PostDiscReq postDiscReq) {
        try {
            // list size check
            if(postDiscReq.getGoodList().size() != 9){
                return new BaseResponse<>(POST_DISC_EMPTY_GOODLIST);
            }
            if(postDiscReq.getBadList().size() != 9){
                return new BaseResponse<>(POST_DISC_EMPTY_BADLIST);
            }

            for(int i = 0; i < 9; i++){
                // null check
                if(postDiscReq.getGoodList().get(i).getName() == null){
                    return new BaseResponse<>(POST_DISC_EMPTY_NAME);
                }
                // 특정 범위 안의 discFeatureIdx 값이 입력되었는지 확인
                if(!(postDiscReq.getGoodList().get(i).getDiscFeatureIdx() > i * 4
                        && postDiscReq.getGoodList().get(i).getDiscFeatureIdx() <= (i + 1) * 4)){
                    return new BaseResponse<>(POST_DISC_INVALID_IDX);
                }
            }

            for(int j = 0; j < 9; j++){
                if(postDiscReq.getBadList().get(j).getName() == null){
                    return new BaseResponse<>(POST_DISC_EMPTY_NAME);
                }
                if(!(postDiscReq.getBadList().get(j).getDiscFeatureIdx() > j * 4
                        && postDiscReq.getBadList().get(j).getDiscFeatureIdx() <= (j + 1) * 4)){
                    return new BaseResponse<>(POST_DISC_INVALID_IDX);
                }
            }

            int userIdx = jwtService.getUserIdx();

            // 첫 테스트는 자동으로 대표로 지정
            if(discProvider.checkUserDisc(userIdx) == 0){
                PostUserDiscRes postUserDiscRes = discService.createUserDisc(userIdx, "Y", postDiscReq);
                return new BaseResponse<>(postUserDiscRes);
            }

            PostUserDiscRes postUserDiscRes = discService.createUserDisc(userIdx, "N", postDiscReq);
            return new BaseResponse<>(postUserDiscRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저가 찾는 팀원 업무성향 테스트 API
     * [POST] /app/search-discs
     * @return BaseResponse<PostTeamDiscRes>
     */
    @ApiOperation(value = "유저가 찾는 팀원 업무성향 테스트 API")
    @ApiResponses({
            @ApiResponse(code = 2004, message = "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 2005, message = "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 2006, message = "업무 유형 name을 입력해주세요"),
            @ApiResponse(code = 2007, message = "올바르지 않은 discFeatureIdx입니다.")
    })
    @ResponseBody
    @PostMapping("/search-discs")
    public BaseResponse<PostSearchDiscRes> createSearchDisc(@RequestBody PostDiscReq postDiscReq) {
        try {
            // list size check
            if(postDiscReq.getGoodList().size() != 9){
                return new BaseResponse<>(POST_DISC_EMPTY_GOODLIST);
            }
            if(postDiscReq.getBadList().size() != 9){
                return new BaseResponse<>(POST_DISC_EMPTY_BADLIST);
            }

            for(int i = 0; i < 9; i++){
                // null check
                if(postDiscReq.getGoodList().get(i).getName() == null){
                    return new BaseResponse<>(POST_DISC_EMPTY_NAME);
                }
                // 특정 범위 안의 discFeatureIdx 값이 입력되었는지 확인
                if(!(postDiscReq.getGoodList().get(i).getDiscFeatureIdx() > i * 4
                        && postDiscReq.getGoodList().get(i).getDiscFeatureIdx() <= (i + 1) * 4)){
                    return new BaseResponse<>(POST_DISC_INVALID_IDX);
                }
            }

            for(int j = 0; j < 9; j++){
                if(postDiscReq.getBadList().get(j).getName() == null){
                    return new BaseResponse<>(POST_DISC_EMPTY_NAME);
                }
                if(!(postDiscReq.getBadList().get(j).getDiscFeatureIdx() > j * 4
                        && postDiscReq.getBadList().get(j).getDiscFeatureIdx() <= (j + 1) * 4)){
                    return new BaseResponse<>(POST_DISC_INVALID_IDX);
                }
            }

            int userIdx = jwtService.getUserIdx();

            // 첫 테스트는 자동으로 대표로 지정
            if(discProvider.checkSearchDisc(userIdx) == 0){
                PostSearchDiscRes postSearchDiscRes = discService.createSearchDisc(userIdx, "Y", postDiscReq);
                return new BaseResponse<>(postSearchDiscRes);
            }

            PostSearchDiscRes postSearchDiscRes = discService.createSearchDisc(userIdx, "N", postDiscReq);
            return new BaseResponse<>(postSearchDiscRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }




}
