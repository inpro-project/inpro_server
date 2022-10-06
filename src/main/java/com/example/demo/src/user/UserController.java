package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.PatchPortfolioReq;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostPortfolioReq;
import com.example.demo.src.user.model.PostPortfolioRes;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexNickName;

@Slf4j
@ApiResponses({
        @ApiResponse(code = 1000, message = "요청에 성공하였습니다."),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다.")
})
@RestController
@AllArgsConstructor
@RequestMapping("/app")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * 유저 프로필 수정 API
     * [PATCH] /app/profiles
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 프로필 수정 API", notes = "profileImg와 별개로 patchUserReq는 Content-type을 application/json으로 설정해야 하기 때문에\n 테스트는 Postman에서만 가능합니다. (자세한 사항은 노션 참조)")
    @ApiResponses({
            @ApiResponse(code = 2008, message = "닉네임은 최소 2자, 최대 10자까지 입력 가능합니다."),
            @ApiResponse(code = 2009, message = "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능합니다."),
            @ApiResponse(code = 2010, message = "거주 지역을 입력해주세요."),
            @ApiResponse(code = 2011, message = "직업군을 입력해주세요."),
            @ApiResponse(code = 2012, message = "직업을 입력해주세요."),
            @ApiResponse(code = 2013, message = "관심 분야를 입력해주세요."),
            @ApiResponse(code = 2014, message = "프로필 사진을 입력해주세요.")
    })
    @ResponseBody
    @PatchMapping("/profiles")
    public BaseResponse<String> modifyUser(@RequestPart(value = "profileImg") MultipartFile multipartFile, @RequestPart(value = "patchUserReq") PatchUserReq patchUserReq) throws IOException {
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

            // 직업 유효성 검사
            if(patchUserReq.getJob() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_JOB);
            }

            // 관심 분야 유효성 검사
            if(patchUserReq.getInterests() == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_INTERESTS);
            }

            // 프로필 사진 유효성 검사
            if(multipartFile == null){
                return new BaseResponse<>(PATCH_USER_EMPTY_IMG);
            }

            userService.modifyUser(userIdx, multipartFile, patchUserReq);
            String result = "프로필이 수정되었습니다.";
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
            @ApiResponse(code = 2015, message = "포트폴리오 제목을 입력해주세요."),
            @ApiResponse(code = 2016, message = "올바르지 않은 portfolioCategoryIdx 입니다.")
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
                return new BaseResponse<>(POST_PORTFOLIO_EMPTY_TITLE);
            }

            PostPortfolioRes postPortfolioRes = userService.createPortfolio(userIdx, portfolioCategoryIdx, postPortfolioReq);
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
            @ApiResponse(code = 2015, message = "포트폴리오 제목을 입력해주세요."),
            @ApiResponse(code = 2017, message = "올바르지 않은 portfolioIdx입니다."),
            @ApiResponse(code = 4003, message = "포트폴리오 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/portfolios/{portfolioIdx}")
    public BaseResponse<String> updatePortfolio(@PathVariable("portfolioIdx") int portfolioIdx, @RequestBody PatchPortfolioReq patchPortfolioReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 포트폴리오 제목 유효성 검사
            if(patchPortfolioReq.getTitle() == null){
                return new BaseResponse<>(POST_PORTFOLIO_EMPTY_TITLE);
            }

            userService.updatePortfolio(userIdx, portfolioIdx, patchPortfolioReq);
            String result = "포트폴리오가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
