package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.team.model.*;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
public class TeamController {

    private final TeamProvider teamProvider;
    private final TeamService teamService;
    private final JwtService jwtService;

    /**
     * 팀 등록 API
     * [POST] /app/teams
     * @return BaseResponse<PostTeamRes>
     */
    @ApiOperation(value = "팀 등록 API")
    @ApiResponses({
            @ApiResponse(code = 332, message = "일반 파일은 5개 이하 첨부 가능합니다."),
            @ApiResponse(code = 333, message = "사진 파일은 5개 이하 첨부 가능합니다."),
            @ApiResponse(code = 334, message = "doc(docx), hwp, pdf, xls(xlsx) 확장자의 일반 파일만 업로드 가능합니다."),
            @ApiResponse(code = 335, message = "jpeg, jpg, png, gif, bmp 확장자의 사진 파일만 업로드 가능합니다."),
            @ApiResponse(code = 337, message = "제목을 입력해주세요."),
            @ApiResponse(code = 338, message = "제목은 20글자까지 입력이 가능합니다."),
            @ApiResponse(code = 339, message = "내용을 입력해주세요."),
            @ApiResponse(code = 340, message = "팀 유형을 입력해주세요."),
            @ApiResponse(code = 341, message = "지역을 입력해주세요."),
            @ApiResponse(code = 342, message = "분야를 입력해주세요."),
            @ApiResponse(code = 416, message = "사진 파일 첨부에 실패하였습니다."),
            @ApiResponse(code = 417, message = "일반 파일 첨부에 실패하였습니다."),
            @ApiResponse(code = 419, message = "멤버 등록에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("/teams")
    public BaseResponse<PostTeamRes> createTeam(@RequestPart(value = "postTeamReq") PostTeamReq postTeamReq
            , @RequestPart(value = "repImg", required = false) MultipartFile repImg
            , @RequestPart(value = "teamImgs", required = false) List<MultipartFile> teamImgs
            , @RequestPart(value = "teamFiles", required = false) List<MultipartFile> teamFiles) {
        try{
            // 제목 유효성 검사
            if(postTeamReq.getTitle() == null){
                return new BaseResponse<>(POST_TEAM_EMPTY_TITLE);
            }
            if(postTeamReq.getTitle().length() > 20){ // 20글자를 넘어가는지 체크
                return new BaseResponse<>(POST_TEAM_MAXSIZE_TITLE);
            }

            // 내용 유효성 검사
            if(postTeamReq.getContent() == null){
                return new BaseResponse<>(POST_TEAM_EMPTY_CONTENT);
            }

            // 팀 유형 유효성 검사
            if(postTeamReq.getType() == null){
                return new BaseResponse<>(POST_TEAM_EMPTY_TYPE);
            }

            // 지역 유효성 검사
            if(postTeamReq.getRegion() == null){
                return new BaseResponse<>(POST_TEAM_EMPTY_REGION);
            }

            // 분야 유효성 검사
            if(postTeamReq.getInterests() == null){
                return new BaseResponse<>(POST_TEAM_EMPTY_INTERESTS);
            }

            int userIdx = jwtService.getUserIdx();

            if(repImg != null){
                String originalFilename = repImg.getOriginalFilename();
                int index = originalFilename.lastIndexOf(".");
                String ext = originalFilename.substring(index + 1);
                if(ext == null || !(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif") || ext.equals("bmp"))){
                    return new BaseResponse<>(POST_IMG_EXT);
                }
            }

            if(teamImgs != null){
                // 이미지 파일 개수 제한 유효성 검사
                if(teamImgs.size() > 5){
                    return new BaseResponse<>(POST_IMG_MAX);
                }
                else {
                    // 이미지 파일 형식 유효성 검사 - 하나라도 올바르지 않으면 전체 업로드 불가능
                    for(int i = 0; i < teamImgs.size(); i++){
                        String originalFilename = teamImgs.get(i).getOriginalFilename();
                        int index = originalFilename.lastIndexOf(".");
                        String ext = originalFilename.substring(index + 1);
                        if(ext == null || !(ext.equals("jpeg") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif") || ext.equals("bmp"))){
                            return new BaseResponse<>(POST_IMG_EXT);
                        }
                    }
                }
            }

            if(teamFiles != null){
                // 일반 파일 개수 제한 유효성 검사
                if(teamFiles.size() > 5){
                    return new BaseResponse<>(POST_FILE_MAX);
                }
                else {
                    // 일반 파일 형식 유효성 검사 - 하나라도 올바르지 않으면 전체 업로드 불가능
                    for(int i = 0; i < teamFiles.size(); i++){
                        String originalFilename = teamFiles.get(i).getOriginalFilename();
                        int index = originalFilename.lastIndexOf(".");
                        String ext = originalFilename.substring(index + 1);
                        if(ext == null || !(ext.equals("doc") || ext.equals("docx") || ext.equals("hwp") || ext.equals("pdf") || ext.equals("xls") || ext.equals("xlsx"))){
                            return new BaseResponse<>(POST_FILE_EXT);
                        }
                    }
                }
            }

            // 팀 제목, 내용, 팀 유형, 지역, 분야 등록
            PostTeamRes postTeamRes = teamService.createTeam(userIdx, postTeamReq);

            // 현재 유저를 멤버의 리더로 등록
            teamService.createTeamLeader(postTeamRes.getTeamIdx(), userIdx);

            // 대표 이미지 파일 추가
            if(repImg != null){
                teamService.createTeamRepImg(postTeamRes.getTeamIdx(), repImg);
            }

            // 이미지 파일 추가
            if(teamImgs != null){
                teamService.createTeamImgs(postTeamRes.getTeamIdx(), teamImgs);
            }

            // 일반 파일 추가
            if(teamFiles != null){
                teamService.createTeamFiles(postTeamRes.getTeamIdx(), teamFiles);
            }

            return new BaseResponse<>(postTeamRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀원 수락 API
     * [POST] /app/members
     * @return BaseResponse<PostMemberRes>
     */
    @ApiOperation(value = "팀원 수락 API")
    @ApiResponses({
            @ApiResponse(code = 326, message = "유효하지 않은 유저 인덱스입니다."),
            @ApiResponse(code = 344, message = "유효하지 않은 팀 인덱스입니다."),
            @ApiResponse(code = 348, message = "이미 멤버로 추가된 유저입니다.")
    })
    @ResponseBody
    @PostMapping("/members")
    public BaseResponse<PostMemberRes> createMember(@RequestBody PostMemberReq postMemberReq){
        try {
            int leaderIdx = jwtService.getUserIdx();

            // 팀원 역할이 따로 주어지지 않으면 팀원으로 저장
            if(postMemberReq.getRole() == null){
                postMemberReq.setRole("팀원");
            }

            PostMemberRes postMemberRes = teamService.createMember(leaderIdx, postMemberReq);
            return new BaseResponse<>(postMemberRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저가 등록한 팀 전체 조회 API
     * [GET] /app/teams
     * @return BaseResponse<GetTeamProfileRes>
     */
    @ApiOperation(value = "유저가 등록한 팀 전체 조회 API")
    @ResponseBody
    @GetMapping("/teams")
    public BaseResponse<List<GetTeamsRes>> getTeams(){
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetTeamsRes> getTeamsResList = teamProvider.getTeams(userIdx);
            return new BaseResponse<>(getTeamsResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 특정 팀 상세 조회 API
     * [GET] /app/teams/:teamIdx
     * @return BaseResponse<List<GetTeamRes>>
     */
    @ApiOperation(value = "특정 팀 상세 조회 API")
    @ApiResponse(code = 344, message = "유효하지 않은 팀 인덱스입니다.")
    @ResponseBody
    @GetMapping("/teams/{teamIdx}")
    public BaseResponse<List<GetTeamRes>> getTeam(@PathVariable("teamIdx") int teamIdx){
        try {
            jwtService.getUserIdx();
            List<GetTeamRes> getTeamRes = teamProvider.getTeam(teamIdx);
            return new BaseResponse<>(getTeamRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀 댓글 전체 조회 API
     * [GET] /app/comments/:teamIdx
     * @return BaseResponse<List<GetCommentsRes>>
     */
    @ApiOperation(value = "팀 댓글 전체 조회 API")
    @ApiResponse(code = 344, message = "유효하지 않은 팀 인덱스입니다.")
    @ResponseBody
    @GetMapping("/comments/{teamIdx}")
    public BaseResponse<List<GetCommentsRes>> getComments(@PathVariable("teamIdx") int teamIdx){
        try {
            jwtService.getUserIdx();
            List<GetCommentsRes> getCommentsResList = teamProvider.getComments(teamIdx);
            return new BaseResponse<>(getCommentsResList);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 댓글 생성 API
     * [POST] /app/comments
     * @return BaseResponse<PostCommentRes>
     */
    @ApiOperation(value = "댓글 생성 API", notes = "parentIdx = 상위 댓글이 없는 경우(그냥 댓글인 경우)에는 0, 그 외(대댓글인 경우)는 상위 댓글의 댓글 식별자(commentIdx)")
    @ApiResponses({
        @ApiResponse(code = 344, message = "유효하지 않은 팀 인덱스입니다."),
        @ApiResponse(code = 349, message = "댓글 내용을 입력해주세요."),
        @ApiResponse(code = 350, message = "팀 인덱스를 입력해주세요."),
        @ApiResponse(code = 351, message = "유효하지 않은 댓글 인덱스(parentIdx)입니다.")

    })
    @ResponseBody
    @PostMapping("/comments")
    public BaseResponse<PostCommentRes> createComment(@RequestBody PostCommentReq postCommentReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 댓글 내용 유효성 검사
            if(postCommentReq.getContent() == null){
                return new BaseResponse<>(COMMENT_EMPTY_CONTENT);
            }

            // 팀 인덱스 유효성 검사
            if(Integer.toString(postCommentReq.getTeamIdx()) == null){
                return new BaseResponse<>(POST_COMMENT_EMPTY_TEAMIDX);
            }

            PostCommentRes postCommentRes = teamService.createComment(userIdx, postCommentReq);
            return new BaseResponse<>(postCommentRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 댓글 삭제 API
     * [DELETE] /app/comments/:commentIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "댓글 삭제 API", notes = "성공시 '댓글이 삭제되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 352, message = "올바르지 않은 댓글 인덱스입니다."),
            @ApiResponse(code = 424, message = "댓글 삭제에 실패하였습니다.")
    })
    @ResponseBody
    @DeleteMapping("/comments/{commentIdx}")
    public BaseResponse<String> deleteComment(@PathVariable("commentIdx") int commentIdx){
        try {
            int userIdx = jwtService.getUserIdx();
            teamService.deleteComment(commentIdx, userIdx);

            String result = "댓글이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 댓글 수정 API
     * [PATCH] /app/comments/:commentIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "댓글 수정 API", notes = "성공시 '댓글이 수정되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 349, message = "댓글 내용을 입력해주세요."),
            @ApiResponse(code = 352, message = "올바르지 않은 댓글 인덱스입니다."),
            @ApiResponse(code = 425, message = "댓글 수정에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/comments/{commentIdx}")
    public BaseResponse<String> updateComment(@PathVariable("commentIdx") int commentIdx, @RequestBody PatchCommentReq patchCommentReq){
        try {
            int userIdx = jwtService.getUserIdx();

            // 댓글 내용 유효성 검사
            if(patchCommentReq.getContent() == null){
                return new BaseResponse<>(COMMENT_EMPTY_CONTENT);
            }

            teamService.updateComment(commentIdx, userIdx, patchCommentReq);

            String result = "댓글이 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 팀원 모집 완료 API
     * [PATCH] /app/team-deadlines/:teamIdx
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "팀원 모집 완료 API", notes = "성공시 '팀원 모집이 완료되었습니다.' 출력")
    @ApiResponses({
            @ApiResponse(code = 344, message = "유효하지 않은 팀 인덱스입니다."),
            @ApiResponse(code = 426, message = "팀 모집 완료에 실패하였습니다.")
    })
    @ResponseBody
    @PatchMapping("/team-deadlines/{teamIdx}")
    public BaseResponse<String> teamDeadline(@PathVariable("teamIdx") int teamIdx){
        try {
            int leaderIdx = jwtService.getUserIdx();

            teamService.teamDeadline(teamIdx, leaderIdx);

            String result = "팀원 모집이 완료되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
