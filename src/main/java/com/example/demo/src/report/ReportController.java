package com.example.demo.src.report;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.report.model.GetBlockedUserRes;
import com.example.demo.src.report.model.PostBlockRes;
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
public class ReportController {

    private final ReportService reportService;

    private final ReportProvider reportProvider;

    private final JwtService jwtService;

    /**
     * 유저 차단 API
     * [POST] /app/blocks/:blockedUserIdx
     * @return BaseResponse<PostBlockRes>
     */
    @ApiOperation(value = "유저 차단 API")
    @ApiResponses({
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 329, message = "이미 차단을 누른 유저입니다."),
            @ApiResponse(code = 414, message = "유저 차단에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("/blocks/{blockedUserIdx}")
    public BaseResponse<PostBlockRes> createBlock(@PathVariable("blockedUserIdx") int blockedUserIdx) {
        try {
            int userIdx = jwtService.getUserIdx();
            PostBlockRes postBlockRes = reportService.createBlock(userIdx, blockedUserIdx);
            return new BaseResponse<>(postBlockRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저 차단 해제 API
     * [PATCH] /app/blocks/:blockedUserIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "유저 차단 해제 API", notes = "성공 시 result로 '유저 차단이 해제되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 330, message = "기존에 차단을 하지 않은 유저입니다."),
            @ApiResponse(code = 415, message = "유저 차단 해제에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/blocks/{blockedUserIdx}")
    public BaseResponse<String> deleteBlock(@PathVariable("blockedUserIdx") int blockedUserIdx) {
        try {
            int userIdx = jwtService.getUserIdx();

            reportService.deleteBlock(userIdx, blockedUserIdx);
            String result = "유저 차단이 해제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 차단한 유저 조회 API
     * [GET] /app/blocks
     * @return BaseResponse<List<GetBlockedUserRes>>
     */
    @ApiOperation(value = "차단한 유저 조회 API")
    @ResponseBody
    @GetMapping("/blocks")
    public BaseResponse<List<GetBlockedUserRes>> getBlockedUsers(){
        try {
            int userIdx = jwtService.getUserIdx();

            List<GetBlockedUserRes> getBlockedUserResList = reportProvider.getBlockedUsers(userIdx);
            return new BaseResponse<>(getBlockedUserResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
