package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.match.model.*;
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
     * 나와 매칭된 유저 조회 API
     * [GET] /app/matched-users
     * @return BaseResponse<List<GetMatchedUserRes>>
     */
    @ApiOperation(value = "나와 매칭된 유저 조회 API")
    @ResponseBody
    @GetMapping("/matched-users")
    public BaseResponse<List<GetMatchedUserRes>> getMatchedUsers(){
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetMatchedUserRes> getMatchedUserResList = matchProvider.getMatchedUsers(userIdx);
            return new BaseResponse<>(getMatchedUserResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀원 매칭 필터링 등록 API
     * [POST] /app/user-filters
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "팀원 매칭 필터링 등록 API", notes = "성공시 '팀원 매칭 필터링이 등록되었습니다.' 출력")
    @ResponseBody
    @PostMapping("/user-filters")
    public BaseResponse<String> createUserFilter(@RequestBody PostUserFilterReq postUserFilterReq){
        try {
            int userIdx = jwtService.getUserIdx();

            if(postUserFilterReq.getAgeRange() != null){
                matchService.createAgeRangeFilter(userIdx, postUserFilterReq.getAgeRange());
            }

            if(postUserFilterReq.getRegion() != null){
                matchService.createRegionFilter(userIdx, postUserFilterReq.getRegion());
            }

            if(postUserFilterReq.getOccupation() != null){
                matchService.createOccupationFilter(userIdx, postUserFilterReq.getOccupation());
            }

            if(postUserFilterReq.getInterests() != null){
                matchService.createInterestsFilter(userIdx, postUserFilterReq.getInterests());
            }

            String result = "팀원 매칭 필터링이 등록되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀원 매칭 필터링 조회 API
     * [GET] /app/user-filters
     * @return BaseResponse<List<GetUserFilterRes>>
     */
    @ApiOperation(value = "팀원 매칭 필터링 조회 API")
    @ResponseBody
    @GetMapping("/user-filters")
    public BaseResponse<List<GetUserFilterRes>> getUserFilters(){
        try {
            int userIdx = jwtService.getUserIdx();

            List<GetUserFilterRes> getUserFilterResList = matchProvider.getUserFilters(userIdx);
            return new BaseResponse<>(getUserFilterResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀원 매칭 필터링 수정 API
     * [PATCH] /app/user-filters
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "팀원 매칭 필터링 수정 API", notes = "성공시 '팀원 매칭 필터링이 수정되었습니다.' 출력")
    @ResponseBody
    @PatchMapping("/user-filters")
    public BaseResponse<String> updateUserFilter(@RequestBody PatchUserFilterReq patchUserFilterReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 필터링 추가 (새로 추가 or status 업데이트)
            if(patchUserFilterReq.getAgeRangeInsert() != null){
                matchService.updateAgeRangeFilter(userIdx, patchUserFilterReq.getAgeRangeInsert());
            }

            if(patchUserFilterReq.getRegionInsert() != null){
                matchService.updateRegionFilter(userIdx, patchUserFilterReq.getRegionInsert());
            }

            if(patchUserFilterReq.getOccupationInsert() != null){
                matchService.updateOccupationFilter(userIdx, patchUserFilterReq.getOccupationInsert());
            }

            if(patchUserFilterReq.getInterestsInsert() != null){
                matchService.updateInterestsFilter(userIdx, patchUserFilterReq.getInterestsInsert());
            }

            // 필터링 삭제
            if(patchUserFilterReq.getAgeRangeDelete() != null){
                matchService.deleteUserFilter(userIdx, patchUserFilterReq.getAgeRangeDelete());
            }

            if(patchUserFilterReq.getRegionDelete() != null){
                matchService.deleteUserFilter(userIdx, patchUserFilterReq.getRegionDelete());
            }

            if(patchUserFilterReq.getOccupationDelete() != null){
                matchService.deleteUserFilter(userIdx, patchUserFilterReq.getOccupationDelete());
            }

            if(patchUserFilterReq.getInterestsDelete() != null){
                matchService.deleteUserFilter(userIdx, patchUserFilterReq.getInterestsDelete());
            }

            String result = "팀원 매칭 필터링이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀 매칭 필터링 등록 API
     * [POST] /app/team-filters
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "팀 매칭 필터링 등록 API", notes = "성공시 '팀 매칭 필터링이 등록되었습니다.' 출력")
    @ResponseBody
    @PostMapping("/team-filters")
    public BaseResponse<String> createTeamFilter(@RequestBody PostTeamFilterReq postTeamFilterReq){
        try {
            int userIdx = jwtService.getUserIdx();

           if(postTeamFilterReq.getType() != null){
               matchService.createTeamTypeFilter(userIdx, postTeamFilterReq.getType());
           }

            if(postTeamFilterReq.getRegion() != null){
                matchService.createTeamRegionFilter(userIdx, postTeamFilterReq.getRegion());
            }

            if(postTeamFilterReq.getInterests() != null){
                matchService.createTeamInterestsFilter(userIdx, postTeamFilterReq.getInterests());
            }

            String result = "팀 매칭 필터링이 등록되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀 매칭 필터링 조회 API
     * [GET] /app/team-filters
     * @return BaseResponse<List<GetTeamFilterRes>>
     */
    @ApiOperation(value = "팀 매칭 필터링 조회 API")
    @ResponseBody
    @GetMapping("/team-filters")
    public BaseResponse<List<GetTeamFilterRes>> getTeamFilters(){
        try {
            int userIdx = jwtService.getUserIdx();

            List<GetTeamFilterRes> getTeamFilterResList = matchProvider.getTeamFilters(userIdx);
            return new BaseResponse<>(getTeamFilterResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀 매칭 필터링 수정 API
     * [PATCH] /app/team-filters
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "팀 매칭 필터링 수정 API", notes = "성공시 '팀 매칭 필터링이 수정되었습니다.' 출력")
    @ResponseBody
    @PatchMapping("/team-filters")
    public BaseResponse<String> updateTeamFilter(@RequestBody PatchTeamFilterReq patchTeamFilterReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 필터링 추가(새로 추가 or status 업데이트)
            if(patchTeamFilterReq.getTypeInsert() != null){
                matchService.updateTeamTypeFilter(userIdx, patchTeamFilterReq.getTypeInsert());
            }

            if(patchTeamFilterReq.getRegionInsert() != null){
                matchService.updateTeamRegionFilter(userIdx, patchTeamFilterReq.getRegionInsert());
            }

            if(patchTeamFilterReq.getInterestsInsert() != null){
                matchService.updateTeamInterestsFilter(userIdx, patchTeamFilterReq.getInterestsInsert());
            }

            // 필터링 삭제
            if(patchTeamFilterReq.getTypeDelete() != null){
                matchService.deleteTeamFilter(userIdx, patchTeamFilterReq.getTypeDelete());
            }

            if(patchTeamFilterReq.getRegionDelete() != null){
                matchService.deleteTeamFilter(userIdx, patchTeamFilterReq.getRegionDelete());
            }

            if(patchTeamFilterReq.getInterestsDelete() != null){
                matchService.deleteTeamFilter(userIdx, patchTeamFilterReq.getInterestsDelete());
            }

            String result = "팀 매칭 필터링이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 나와 매칭된 팀 조회 API
     * [GET] /app/matched-teams
     * @return BaseResponse<List<GetMatchedTeamRes>>
     */
    @ApiOperation(value = "나와 매칭된 팀 조회 API")
    @ResponseBody
    @GetMapping("/matched-teams")
    public BaseResponse<List<GetMatchedTeamRes>> getMatchedTeams(){
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetMatchedTeamRes> getMatchedTeamResList = matchProvider.getMatchedTeams(userIdx);
            return new BaseResponse<>(getMatchedTeamResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
