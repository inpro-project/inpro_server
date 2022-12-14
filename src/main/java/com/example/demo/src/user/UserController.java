package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.match.model.SearchDiscXy;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexNickName;

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
public class UserController {

    private final UserProvider userProvider;
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 유저 프로필 수정 API
     * [PATCH] /app/profiles
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 프로필 수정 API", notes = "성공시 '프로필 이미지가 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 308, message = "닉네임은 최소 2자, 최대 10자까지 입력 가능합니다."),
            @ApiResponse(code = 309, message = "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능합니다."),
            @ApiResponse(code = 310, message = "거주 지역을 입력해주세요."),
            @ApiResponse(code = 311, message = "직업군을 입력해주세요."),
            @ApiResponse(code = 313, message = "관심 분야를 입력해주세요."),
            @ApiResponse(code = 405, message = "프로필 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/profiles")
    public BaseResponse<String> modifyUser(@RequestBody PatchUserReq patchUserReq) throws IOException {
        try {
            int userIdx = jwtService.getUserIdx();

            // 닉네임 유효성 검사
            if(patchUserReq.getNickName() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_NICKNAME);
            }
            if(patchUserReq.getNickName().length() < 2 || patchUserReq.getNickName().length() > 10){
                return new BaseResponse<>(PATCH_USER_EMPTY_NICKNAME);
            }
            if(!isRegexNickName(patchUserReq.getNickName())){
                return new BaseResponse<>(PATCH_USER_INVALID_NICKNAME);
            }

            // 거주 지역 유효성 검사
            if(patchUserReq.getRegion() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_REGION);
            }

            // 직업군 유효성 검사
            if(patchUserReq.getOccupation() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_OCCUPATION);
            }

            // 관심 분야 유효성 검사
            if(patchUserReq.getInterests() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_INTERESTS);
            }

            userService.modifyUser(userIdx, patchUserReq);
            String result = "프로필이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 프로필 이미지 수정 API
     * [PATCH] /app/getMyProfileRes-imgs
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 프로필 이미지 수정 API", notes = "성공시 '프로필 이미지가 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 314, message = "프로필 사진을 입력해주세요."),
            @ApiResponse(code = 428, message = "프로필 이미지 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/getMyProfileRes-imgs")
    public BaseResponse<String> modifyUserProfileImg(@RequestPart(value = "profileImg") MultipartFile multipartFile) throws IOException {
        try {
            int userIdx = jwtService.getUserIdx();

            // 프로필 사진 유효성 검사
            if(multipartFile.isEmpty()){
                return new BaseResponse<>(PATCH_USER_EMPTY_IMG);
            }

            userService.modifyUserProfileImg(userIdx, multipartFile);
            String result = "프로필 이미지가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 포트폴리오 등록 API
     * [POST] /app/portfolios/:portfolioCategoryIdx
     * @return BaseResponse<PostPortfolioRes>
     */
    @ApiOperation(value = "포트폴리오 등록 API", notes = "성공시 포트폴리오 인덱스(portfolioIdx) 출력")
    @ApiResponses({
            @ApiResponse(code = 315, message = "포트폴리오 제목을 입력해주세요."),
            @ApiResponse(code = 316, message = "올바르지 않은 portfolioCategoryIdx 입니다.")
    })
    @ResponseBody
    @PostMapping("/portfolios/{portfolioCategoryIdx}")
    public BaseResponse<PostPortfolioRes> createPortfolio(@PathVariable("portfolioCategoryIdx") int portfolioCategoryIdx, @RequestBody PostPortfolioReq postPortfolioReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 포트폴리오 카테고리 인덱스 유효성 검사
            if(portfolioCategoryIdx != 1 && portfolioCategoryIdx != 2 && portfolioCategoryIdx != 3){
                return new BaseResponse<>(POST_PORTFOLIO_INVALID_IDX);
            }

            // 포트폴리오 제목 유효성 검사
            if(postPortfolioReq.getTitle() == null){
                return new BaseResponse<>(PORTFOLIO_EMPTY_TITLE);
            }

            // 카테고리별 첫 포트폴리오는 자동으로 대표로 지정
            if(userProvider.checkPortfolio(userIdx, portfolioCategoryIdx) == 0){
                PostPortfolioRes postPortfolioRes = userService.createPortfolio(userIdx, portfolioCategoryIdx, postPortfolioReq, "Y");
                return new BaseResponse<>(postPortfolioRes);
            }

            PostPortfolioRes postPortfolioRes = userService.createPortfolio(userIdx, portfolioCategoryIdx, postPortfolioReq, "N");
            return new BaseResponse<>(postPortfolioRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 포트폴리오 수정 API
     * [PATCH] /app/portfolios/:portfolioIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "포트폴리오 수정 API", notes = "성공시 '포트폴리오가 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 315, message = "포트폴리오 제목을 입력해주세요."),
            @ApiResponse(code = 317, message = "올바르지 않은 portfolioIdx입니다."),
            @ApiResponse(code = 318, message = "올바르지 않은 대표 여부입니다. (Y나 N만 가능)"),
            @ApiResponse(code = 406, message = "포트폴리오 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/portfolios/{portfolioIdx}")
    public BaseResponse<String> updatePortfolio(@PathVariable("portfolioIdx") int portfolioIdx, @RequestBody PatchPortfolioReq patchPortfolioReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 포트폴리오 제목 유효성 검사
            if(patchPortfolioReq.getTitle() == null){
                return new BaseResponse<>(PORTFOLIO_EMPTY_TITLE);
            }

            // 대표 여부 유효성 검사
            if(patchPortfolioReq.getIsRepPortfolio() != null && !patchPortfolioReq.getIsRepPortfolio().equals("Y") && !patchPortfolioReq.getIsRepPortfolio().equals("N")){
                return new BaseResponse<>(PORTFOLIO_INVALID_ISREP);
            }

            userService.updatePortfolio(userIdx, portfolioIdx, patchPortfolioReq);
            String result = "포트폴리오가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 포트폴리오 삭제 API
     * [DELETE] /app/portfolios/:portfolioIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "포트폴리오 삭제 API", notes = "성공시 '포트폴리오가 삭제되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 317, message = "올바르지 않은 portfolioIdx입니다."),
            @ApiResponse(code = 407, message = "포트폴리오 삭제에 실패하였습니다.")
    })
    @ResponseBody
    @DeleteMapping("/portfolios/{portfolioIdx}")
    public BaseResponse<String> deletePortfolio(@PathVariable("portfolioIdx") int portfolioIdx) {
        try {
            int userIdx = jwtService.getUserIdx();

            userService.deletePortfolio(userIdx, portfolioIdx);
            String result = "포트폴리오가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 포트폴리오 조회 API
     * [GET] /app/portfolios/:userIdx/:portfolioCategoryIdx
     * @return BaseResponse<List<GetPortfolioRes>>
     */
    @ApiOperation(value = "포트폴리오 조회 API")
    @ApiResponses({
            @ApiResponse(code = 317, message = "올바르지 않은 portfolioIdx입니다."),
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
    })
    @ResponseBody
    @GetMapping("/portfolios/{userIdx}/{portfolioCategoryIdx}")
    public BaseResponse<List<GetPortfolioRes>> getPortfolios(@PathVariable("userIdx") int userIdx, @PathVariable("portfolioCategoryIdx") int portfolioCategoryIdx){
        try {
            // 포트폴리오 카테고리 인덱스 유효성 검사
            if(portfolioCategoryIdx != 1 && portfolioCategoryIdx != 2 && portfolioCategoryIdx != 3){
                return new BaseResponse<>(POST_PORTFOLIO_INVALID_IDX);
            }

            List<GetPortfolioRes> getPortfolioRes = userProvider.getPortfolios(userIdx, portfolioCategoryIdx);
            return new BaseResponse<>(getPortfolioRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 프로필 태그 등록 API
     * [POST] /app/usertags
     * @return BaseResponse<PostUserTagRes>
     */
    @ApiOperation(value = "유저 프로필 태그 등록 API", notes = "성공시 등록된 태그의 userTagIdx를 응답으로 줌")
    @ApiResponses({
            @ApiResponse(code = 319, message = "태그 이름을 입력해주세요."),
            @ApiResponse(code = 320, message = "태그는 최대 3개까지 추가 가능합니다."),
            @ApiResponse(code = 321, message = "태그 이름은 5글자 이하로 입력이 가능합니다.")
    })
    @ApiImplicitParam(name = "name", value = "태그 이름", example = "웹개발")
    @ResponseBody
    @PostMapping("/usertags")
    public BaseResponse<PostUserTagRes> createUserTags(@RequestParam String name){
        try {
            int userIdx = jwtService.getUserIdx();

            // 태그 유효성 검사
            if(name == null){
                return new BaseResponse<>(POST_USERTAG_EMPTY_NAME);
            }
            if(userProvider.getNumOfUserTag(userIdx) > 2){
                return new BaseResponse<>(POST_USERTAG_INVALID_SIZE);
            }
            if(name.length() > 5){
                return new BaseResponse<>(POST_USERTAG_INVALID_LENGTH);
            }

            PostUserTagRes postUserTagRes = userService.createUserTags(userIdx, name);
            return new BaseResponse<>(postUserTagRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 프로필 태그 삭제 API
     * [DELETE] /app/usertags/:userTagIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 프로필 태그 삭제 API", notes = "성공시 '태그가 삭제되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 322, message = "올바르지 않은 userTagIdx입니다."),
            @ApiResponse(code = 408, message = "태그 삭제에 실패하였습니다.")
    })
    @ApiImplicitParam(name = "userTagIdx", value = "유저 태그 인덱스", example = "1")
    @ResponseBody
    @DeleteMapping("/usertags/{userTagIdx}")
    public BaseResponse<String> createUserTags(@PathVariable("userTagIdx") int userTagIdx){
        try {
            int userIdx = jwtService.getUserIdx();

            userService.deleteUserTag(userIdx, userTagIdx);

            String result = "태그가 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 나의 프로필 조회 API
     * [GET] /app/profiles
     * @return BaseResponse<GetMyProfileRes>
     */
    @ApiOperation(value = "나의 프로필 조회 API")
    @ResponseBody
    @GetMapping("/profiles")
    public BaseResponse<GetMyProfileRes> getProfile(){
        try {
            int userIdx = jwtService.getUserIdx();
            GetMyProfileRes getMyProfileRes = userProvider.getMyProfile(userIdx);
            return new BaseResponse<>(getMyProfileRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 유저 프로필 상세 조회 API
     * [GET] /app/user-profiles/:userIdx
     * @return BaseResponse<GetUserProfileRes>
     */
    @ApiOperation(value = "특정 유저 프로필 상세 조회 API")
    @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다.")
    @ResponseBody
    @GetMapping("/user-profiles/{userIdx}")
    public BaseResponse<GetUserProfileRes> getUserProfile(@PathVariable("userIdx") int userIdx){
        try {
            int loginUserIdx = jwtService.getUserIdx();
            GetUserProfileRes getUserProfileRes = userProvider.getUserProfile(loginUserIdx, userIdx);
            return new BaseResponse<>(getUserProfileRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 1:1 매칭 유저 프로필 조회 API
     * [GET] /app/user-matches
     * @return BaseResponse<GetUserMatchRes>
     */
    @ApiOperation(value = "1:1 매칭 유저 프로필 조회 API")
    @ApiResponse(code = 413, message = "search disc 조정에 실패하였습니다.")
    @ResponseBody
    @GetMapping("/user-matches")
    public BaseResponse<List<GetUserMatchRes>> getUserMatches(){
        try {
            int userIdx = jwtService.getUserIdx();

            // search disc - 좋아요가 5개 이상인 경우에만 생성 및 조정됨
            SearchDiscXy newSearchDisc = userProvider.getNewSearchDisc(userIdx);
            if(userProvider.checkSearchDisc(userIdx) != 0){
                SearchDiscXy pastSearchDisc = userProvider.getPastSearchDisc(userIdx);
                if(pastSearchDisc.getX() != newSearchDisc.getX() || pastSearchDisc.getY() != newSearchDisc.getY()) {
                    userService.updateSearchDisc(userIdx, newSearchDisc.getX(), newSearchDisc.getY());
                }
            }
            else {
                userService.createSearchDisc(userIdx, newSearchDisc.getX(), newSearchDisc.getY());
            }

            List<GetUserMatchRes> getUserMatchRes = userProvider.getUserMatches(userIdx, newSearchDisc);
            return new BaseResponse<>(getUserMatchRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}