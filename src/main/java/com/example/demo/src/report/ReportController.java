package com.example.demo.src.report;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.report.model.GetBlockedUserRes;
import com.example.demo.src.report.model.PostBlockRes;
import com.example.demo.src.report.model.PostReportReq;
import com.example.demo.src.report.model.PostReportRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    /**
     * 신고하기 API
     * [POST] /app/reports/:reportedUserIdx
     * @return BaseResponse<PostReportRes>
     */
    @ApiOperation(value = "신고하기 API")
    @ApiResponses({
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 331, message = "신고 카테고리를 입력해주세요."),
            @ApiResponse(code = 332, message = "일반 파일은 5개 이하 첨부 가능합니다."),
            @ApiResponse(code = 333, message = "사진 파일은 5개 이하 첨부 가능합니다."),
            @ApiResponse(code = 334, message = "doc(docx), hwp, pdf, xls(xlsx) 확장자의 일반 파일만 업로드 가능합니다."),
            @ApiResponse(code = 335, message = "jpeg, jpg, png, gif, bmp 확장자의 사진 파일만 업로드 가능합니다."),
            @ApiResponse(code = 416, message = "신고 사진 첨부에 실패하였습니다."),
            @ApiResponse(code = 417, message = "신고 파일 첨부에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("/reports/{reportedUserIdx}")
    public BaseResponse<PostReportRes> createReport(@PathVariable("reportedUserIdx") int reportedUserIdx
            , @RequestPart(value = "reportImgs", required = false) List<MultipartFile> reportImgs
            , @RequestPart(value = "reportFiles", required = false) List<MultipartFile> reportFiles
            , @RequestPart(value = "postReportReq") PostReportReq postReportReq) {
        try {
            // 신고 카테고리 유효성 검사
            if(postReportReq.getCategory() == null){
                return new BaseResponse<>(POST_REPORT_EMPTY_CATEGORY);
            }

            int userIdx = jwtService.getUserIdx();

            if(reportImgs != null){
                // 이미지 파일 개수 제한 유효성 검사
                if(reportImgs.size() > 5){
                    return new BaseResponse<>(POST_REPORTIMG_MAX);
                }
                else {
                    // 이미지 파일 형식 유효성 검사 - 하나라도 올바르지 않으면 전체 업로드 불가능
                    for(int i = 0; i < reportImgs.size(); i++){
                        String originalFilename = reportImgs.get(i).getOriginalFilename();
                        int index = originalFilename.lastIndexOf(".");
                        String ext = originalFilename.substring(index + 1);
                        if(ext == null || !(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif") || ext.equals("bmp"))){
                            return new BaseResponse<>(POST_REPORTIMG_EXT);
                        }
                    }
                }
            }

            if(reportFiles != null){
                // 일반 파일 개수 제한 유효성 검사
                if(reportFiles.size() > 5){
                    return new BaseResponse<>(POST_REPORTFILE_MAX);
                }
                else {
                    // 일반 파일 형식 유효성 검사 - 하나라도 올바르지 않으면 전체 업로드 불가능
                    for(int i = 0; i < reportFiles.size(); i++){
                        String originalFilename = reportFiles.get(i).getOriginalFilename();
                        int index = originalFilename.lastIndexOf(".");
                        String ext = originalFilename.substring(index + 1);
                        if(ext == null || !(ext.equals("doc") || ext.equals("docx") || ext.equals("hwp") || ext.equals("pdf") || ext.equals("xls") || ext.equals("xlsx"))){
                            return new BaseResponse<>(POST_REPORTFILE_EXT);
                        }
                    }
                }
            }

            // 신고 카테고리, 내용 추가
            PostReportRes postReportRes = reportService.createReport(userIdx, reportedUserIdx, postReportReq);

            // 이미지 파일 추가
            if(reportImgs != null){
                reportService.createReportImgs(postReportRes.getReportIdx(), reportImgs);
            }

            // 일반 파일 추가
            if(reportFiles != null){
                reportService.createReportFiles(postReportRes.getReportIdx(), reportFiles);
            }

            return new BaseResponse<>(postReportRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
