package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.disc.model.*;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiImplicitParam;
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
        @ApiResponse(code = 200, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 400, message = "데이터베이스 연결에 실패하였습니다.")
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
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 304, message = "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 305, message = "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 306, message = "업무 유형 name을 입력해주세요"),
            @ApiResponse(code = 307, message = "올바르지 않은 discFeatureIdx입니다.")
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
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 304, message = "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 305, message = "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
            @ApiResponse(code = 306, message = "업무 유형 name을 입력해주세요"),
            @ApiResponse(code = 307, message = "올바르지 않은 discFeatureIdx입니다.")
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

    /**
     * User Disc 결과 상세 조회 API
     * [GET] /app/user-discs/:userDiscIdx
     * @return BaseResponse<GetDiscResultRes>
     */
    @ApiOperation(value = "User Disc 결과 상세 조회 API")
    @ApiResponses({
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 323, message = "올바르지 않은 userDiscIdx입니다.")
    })
    @ApiImplicitParam(name = "userDiscIdx", value = "유저 업무 성향 인덱스", example = "1")
    @ResponseBody
    @GetMapping("/user-discs/{userDiscIdx}")
    public BaseResponse<GetUserDiscResultRes> getUserDiscResult(@PathVariable("userDiscIdx") int userDiscIdx) {
        try {
            int userIdx = jwtService.getUserIdx();
            GetUserDiscResultRes getDiscResultRes = discProvider.getUserDiscResult(userIdx, userDiscIdx);
            return new BaseResponse<>(getDiscResultRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * Search Disc 결과 상세 조회 API
     * [GET] /app/search-discs/:searchDiscIdx
     * @return BaseResponse<GetDiscResultRes>
     */
    @ApiOperation(value = "Search Disc 결과 상세 조회 API")
    @ApiResponses({
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 324, message = "올바르지 않은 searchDiscIdx입니다.")
    })
    @ApiImplicitParam(name = "searchDiscIdx", value = "유저가 찾는 업무 성향 인덱스", example = "1")
    @ResponseBody
    @GetMapping("/search-discs/{searchDiscIdx}")
    public BaseResponse<GetSearchDiscResultRes> getSearchDiscResult(@PathVariable("searchDiscIdx") int searchDiscIdx) {
        try {
            int userIdx = jwtService.getUserIdx();
            GetSearchDiscResultRes getDiscResultRes = discProvider.getSearchDiscResult(userIdx, searchDiscIdx);
            return new BaseResponse<>(getDiscResultRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * User Disc 이름 등록 및 수정
     * [PATCH] /app/user-discs/:userDiscIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "User Disc 이름 등록 및 수정 API", notes = "성공시 'User Disc 이름이 등록 및 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 323, message = "올바르지 않은 userDiscIdx입니다."),
            @ApiResponse(code = 412, message = "user disc 이름 등록 및 수정에 실패하였습니다.")
    })
    @ApiImplicitParam(name = "name", value = "설정할 이름", example = "user disc")
    @ResponseBody
    @PatchMapping("/user-discs/{userDiscIdx}")
    public BaseResponse<String> createUserDiscName(@PathVariable("userDiscIdx") int userDiscIdx, @RequestParam(value = "name", required = false) String name){
        try {
            int userIdx = jwtService.getUserIdx();
            discService.createUserDiscName(userIdx, userDiscIdx, name);
            String result = "User Disc 이름이 등록 및 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * Search Disc 이름 등록 및 수정
     * [PATCH] /app/search-discs/:searchDiscIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "Search Disc 이름 등록 및 수정 API", notes = "성공시 'Search Disc 이름이 등록 및 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 324, message = "올바르지 않은 searchDiscIdx입니다."),
            @ApiResponse(code = 413, message = "search disc 이름 등록 및 수정에 실패하였습니다.")
    })
    @ApiImplicitParam(name = "name", value = "설정할 이름", example = "search disc")
    @ResponseBody
    @PatchMapping("/search-discs/{searchDiscIdx}")
    public BaseResponse<String> createSearchDiscName(@PathVariable("searchDiscIdx") int searchDiscIdx, @RequestParam(value = "name", required = false) String name){
        try {
            int userIdx = jwtService.getUserIdx();
            discService.createSearchDiscName(userIdx, searchDiscIdx, name);
            String result = "Search Disc 이름이 등록 및 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * User Disc 결과 리스트 API
     * [GET] /app/user-discs
     * @return BaseResponse<List<GetUserDiscResultRes>>
     */
    @ApiOperation(value = "User Disc 결과 리스트 API")
    @ApiResponses({
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다.")
    })
    @ResponseBody
    @GetMapping("/user-discs")
    public BaseResponse<List<GetUserDiscResultRes>> getUserDiscResultList() {
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetUserDiscResultRes> getUserDiscResultRes = discProvider.getUserDiscResultList(userIdx);
            return new BaseResponse<>(getUserDiscResultRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * Search Disc 결과 리스트 API
     * [GET] /app/search-discs
     * @return BaseResponse<GetSearchDiscResultRes>
     */
    @ApiOperation(value = "Search Disc 결과 리스트 API")
    @ApiResponses({
            @ApiResponse(code = 301, message = "JWT를 입력해주세요."),
            @ApiResponse(code = 302, message = "유효하지 않은 JWT입니다.")
    })
    @ResponseBody
    @GetMapping("/search-discs")
    public BaseResponse<List<GetSearchDiscResultRes>> getSearchDiscResultList() {
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetSearchDiscResultRes> getSearchDiscResultRes = discProvider.getSearchDiscResultList(userIdx);
            return new BaseResponse<>(getSearchDiscResultRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}