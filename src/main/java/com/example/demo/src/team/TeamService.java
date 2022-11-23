package com.example.demo.src.team;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import com.example.demo.src.team.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamProvider teamProvider;
    private final TeamDao teamDao;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PostTeamRes createTeam(int userIdx, PostTeamReq postTeamReq) throws BaseException {
        try {
            int teamIdx = teamDao.createTeam(userIdx, postTeamReq);
            return new PostTeamRes(teamIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamLeader(int teamIdx, int userIdx) throws BaseException {
        try {
            int result = teamDao.createTeamLeader(teamIdx, userIdx);
            if(result == 0){
                throw new BaseException(FAIL_MEMBER);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamRepImg(int teamIdx, MultipartFile multipartFile) throws BaseException {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String teamImgUrl = getTeamImgUrl(multipartFile);
            int result = teamDao.createTeamImg(teamIdx, originalFileName, teamImgUrl, "Y");
            if(result == 0){
                throw new BaseException(FAIL_IMG);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamImgs(int teamIdx, List<MultipartFile> multipartFile) throws BaseException {
        try {
            for(int i = 0; i < multipartFile.size(); i++){
                String originalFileName = multipartFile.get(i).getOriginalFilename();
                String teamImgUrl = getTeamImgUrl(multipartFile.get(i));
                int result = teamDao.createTeamImg(teamIdx, originalFileName, teamImgUrl, "N");
                if(result == 0){
                    throw new BaseException(FAIL_IMG);
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getTeamImgUrl(MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "teamImg/" + storeFileName;

        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
        String teamImgUrl = amazonS3.getUrl(bucket, key).toString();

        return teamImgUrl;
    }

    public void createTeamFiles(int teamIdx, List<MultipartFile> multipartFile) throws BaseException {
        try {
            for(int i = 0; i < multipartFile.size(); i++){
                String originalFileName = multipartFile.get(i).getOriginalFilename();
                String teamFileUrl = getTeamFileUrl(multipartFile.get(i));
                int result = teamDao.createTeamFile(teamIdx, originalFileName, teamFileUrl);
                if(result == 0){
                    throw new BaseException(FAIL_FILE);
                }
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getTeamFileUrl(MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "teamFile/" + storeFileName;

        amazonS3.putObject(bucket, key, multipartFile.getInputStream(), objectMetadata);
        String teamFileUrl = amazonS3.getUrl(bucket, key).toString();

        return teamFileUrl;
    }

    public PostMemberRes createMember(int leaderIdx, PostMemberReq postMemberReq) throws BaseException {
        // 팀원으로 추가할 유저 유효성 검사
        if(teamProvider.checkUserIdx(postMemberReq.getUserIdx()) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 현재 유저가 팀을 만든 유저가 맞는지 확인, 유효한 팀 인덱스인지 확인
        if(teamProvider.checkTeamIdxByLeader(postMemberReq.getTeamIdx(), leaderIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 팀에 이미 포함된 멤버인지 확인
        if(teamProvider.checkPreTeamMember(postMemberReq.getTeamIdx(), postMemberReq.getUserIdx()) == 1){
            throw new BaseException(MEMBER_INVALID_USERIDX);
        }
        try {
            int teamMemberIdx = teamDao.createMember(postMemberReq);
            return new PostMemberRes(teamMemberIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCommentRes createComment(int userIdx, PostCommentReq postCommentReq) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(teamProvider.checkTeamIdx(postCommentReq.getTeamIdx()) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 유효한 댓글 인덱스인지 확인
        if(postCommentReq.getParentIdx() != 0){ // 대댓글인 경우에만 확인
            if(teamProvider.checkCommentIdx(postCommentReq.getParentIdx()) == 0){
                throw new BaseException(INVALID_COMMENTIDX);
            }
        }

        try {
            int commentIdx = teamDao.createComment(userIdx, postCommentReq);
            return new PostCommentRes(commentIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteComment(int commentIdx, int userIdx) throws BaseException {
        // 댓글 인덱스 유효성 검사(현재 로그인한 사용자의 댓글이 맞는지도 확인)
        if(teamProvider.checkCommentByUserIdx(commentIdx, userIdx) == 0){
            throw new BaseException(DELETE_COMMENT_INVALID_COMMENTIDX);
        }

        try {
            int result = teamDao.deleteComment(commentIdx);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_COMMENT);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
