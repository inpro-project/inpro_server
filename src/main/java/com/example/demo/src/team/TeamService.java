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
import static com.example.demo.config.Constant.*;

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

    private final double weights [] = {WEIGHT1, WEIGHT2, WEIGHT3};
    private final double percentWeights [] = {PERCENTWEIGHT1, PERCENTWEIGHT2};

    public PostTeamRes createTeam(int userIdx, int chatRoomIdx, PostTeamReq postTeamReq) throws BaseException {
        try {
            int teamIdx = teamDao.createTeam(userIdx, chatRoomIdx, postTeamReq);
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

    public void deleteTeam(int teamIdx, int leaderIdx) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(teamProvider.checkTeamDeleted(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 현재 유저가 팀을 만든 유저가 맞는지 확인
        if(teamProvider.checkTeamIdxByLeader(teamIdx, leaderIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            int result = teamDao.deleteTeam(teamIdx);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_TEAM);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostMemberRes createMember(int leaderIdx, PostMemberReq postMemberReq) throws BaseException {
        // 팀원으로 추가할 유저 유효성 검사
        if(teamProvider.checkUserIdx(postMemberReq.getUserIdx()) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 유효한 팀 인덱스인지 확인
        if(teamProvider.checkTeamActive(postMemberReq.getTeamIdx()) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 현재 유저가 팀을 만든 유저가 맞는지 확인
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
        if(teamProvider.checkTeamActive(postCommentReq.getTeamIdx()) == 0){
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
            throw new BaseException(COMMENT_INVALID_COMMENTIDX);
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

    public void updateComment(int commentIdx, int userIdx, PatchCommentReq patchCommentReq) throws BaseException {
        // 댓글 인덱스 유효성 검사(현재 로그인한 사용자의 댓글이 맞는지도 확인)
        if(teamProvider.checkCommentByUserIdx(commentIdx, userIdx) == 0){
            throw new BaseException(COMMENT_INVALID_COMMENTIDX);
        }

        try {
            int result = teamDao.updateComment(commentIdx, patchCommentReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_COMMENT);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void teamDeadline(int teamIdx, int leaderIdx) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(teamProvider.checkTeamActive(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 현재 유저가 팀을 만든 유저가 맞는지 확인
        if(teamProvider.checkTeamIdxByLeader(teamIdx, leaderIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            int result = teamDao.teamDeadline(teamIdx);
            if(result == 0){
                throw new BaseException(FAIL_TEAM_DEADLINE);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void teamFinish(int teamIdx, int leaderIdx) throws BaseException {
        // 팀원 모집을 완료한 팀 인덱스인지 확인
        if(teamProvider.checkTeamInActive(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 현재 유저가 팀을 만든 유저가 맞는지 확인
        if(teamProvider.checkTeamIdxByLeader(teamIdx, leaderIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            int result = teamDao.teamFinish(teamIdx);
            if(result == 0){
                throw new BaseException(FAIL_TEAM_FINISH);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public double[] calReview(PostReviewReq postReviewReq) {
        double x = 0.0;
        double y = 0.0;

        for(int i = 0; i < postReviewReq.getReviews().size(); i++) {
            if (postReviewReq.getReviews().get(i).getName().equals("id")) {
                x += weights[0];
                y += weights[2];
            } else if (postReviewReq.getReviews().get(i).getName().equals("di")) {
                x += -weights[0];
                y += weights[2];
            } else if (postReviewReq.getReviews().get(i).getName().equals("cs")) {
                x += -weights[0];
                y += -weights[2];
            } else if (postReviewReq.getReviews().get(i).getName().equals("sc")) {
                x += weights[0];
                y += -weights[2];
            } else if (postReviewReq.getReviews().get(i).getName().equals("is")) {
                x += weights[2];
                y += weights[0];
            } else if (postReviewReq.getReviews().get(i).getName().equals("si")) {
                x += weights[2];
                y += -weights[0];
            } else if (postReviewReq.getReviews().get(i).getName().equals("dc")) {
                x += -weights[2];
                y += weights[0];
            } else if (postReviewReq.getReviews().get(i).getName().equals("cd")) {
                x += -weights[2];
                y += -weights[0];
            } else if (postReviewReq.getReviews().get(i).getName().equals("i")) {
                x += weights[1];
                y += weights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("d")) {
                x += -weights[1];
                y += weights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("c")) {
                x += -weights[1];
                y += -weights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("s")) {
                x += weights[1];
                y += -weights[1];
            }
        }
        double[] reviewDisc = {x, y};
        return reviewDisc;
    }

    // disc 유형별 비중(퍼센트) 계산
    public double[] calDiscPercent(PostReviewReq postReviewReq) throws BaseException {
        double dWeight = 0;
        double iWeight = 0;
        double sWeight = 0;
        double cWeight = 0;

        // 적합인 경우 가중치 조정
        for(int i = 0; i < postReviewReq.getReviews().size(); i++){
            if (postReviewReq.getReviews().get(i).getName().equals("id")) {
                iWeight += percentWeights[0];
                dWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("di")) {
                dWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("cs")) {
                cWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("sc")) {
                sWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("is")) {
                iWeight += percentWeights[0];
                sWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("si")) {
                sWeight += percentWeights[0];
                iWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("dc")) {
                dWeight += percentWeights[0];
                cWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("cd")) {
                cWeight += percentWeights[0];
                dWeight += percentWeights[1];
            } else if (postReviewReq.getReviews().get(i).getName().equals("i")) {
                iWeight += 1;
            } else if (postReviewReq.getReviews().get(i).getName().equals("d")) {
                dWeight += 1;
            } else if (postReviewReq.getReviews().get(i).getName().equals("c")) {
                cWeight += 1;
            } else if (postReviewReq.getReviews().get(i).getName().equals("s")) {
                sWeight += 1;
            }
        }

        // 유형별 비중 계산
        double totalWeight = dWeight + iWeight + sWeight + cWeight;
        double dPercent = (dWeight/totalWeight) * 100;
        double iPercent = (iWeight/totalWeight) * 100;
        double sPercent = (sWeight/totalWeight) * 100;
        double cPercent = (cWeight/totalWeight) * 100;

        double[] discPercent = {dPercent, iPercent, sPercent, cPercent};
        return discPercent;
    }

    public PostReviewRes createReview(int reviewerIdx, int reviewingIdx, PostReviewReq postReviewReq) throws BaseException {
        // 리뷰를 남길 유저 인덱스 유효성 검사
        if(teamProvider.checkUserIdx(reviewingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 프로젝트가 종료된 팀인지 확인(유효한 팀 인덱스인지 확인)
        if(teamProvider.checkTeamFinish(postReviewReq.getTeamIdx()) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 리뷰를 남기는 유저와 평가 대상 유저가 팀에 속하는지 확인
        if(teamProvider.checkPreTeamMember(postReviewReq.getTeamIdx(), reviewerIdx) == 0
            || teamProvider.checkPreTeamMember(postReviewReq.getTeamIdx(), reviewingIdx) == 0){
            throw new BaseException(POST_REVIEW_INVALID_USERIDX);
        }

        // 특정 팀의 팀원에게 이미 리뷰를 남긴 기록이 있는지 확인
        if(teamProvider.checkPastReview(postReviewReq.getTeamIdx(), reviewerIdx, reviewingIdx) == 1){
            throw new BaseException(POST_REVIEW_INVALID_REVIEWERIDX);
        }

        try {
            // 팀원 평가 x, y 좌표
            double[] reviewDisc = calReview(postReviewReq);
            double[] percentList = calDiscPercent(postReviewReq);

            // 원래 유저 x, y 좌표
            PastUserDisc pastUserDisc = teamProvider.getPastUserDisc(reviewingIdx);

            // 해당 유저의 기존 disc 기록이 있는 경우에만 조정 가능
            if(Integer.toString(pastUserDisc.getUserDiscIdx()) != null){
                // x, y 좌표 조정
                double x = (reviewDisc[0] - pastUserDisc.getX()) / 10;
                double y = (reviewDisc[1] - pastUserDisc.getY()) / 10;
                x += pastUserDisc.getX();
                y += pastUserDisc.getY();

                // 일치 퍼센트 조정
                double dPercent = (percentList[0] - pastUserDisc.getDPercent()) / 10;
                double iPercent = (percentList[1] - pastUserDisc.getIPercent()) / 10;
                double sPercent = (percentList[2] - pastUserDisc.getSPercent()) / 10;
                double cPercent = (percentList[3] - pastUserDisc.getCPercent()) / 10;
                dPercent += pastUserDisc.getDPercent();
                iPercent += pastUserDisc.getIPercent();
                sPercent += pastUserDisc.getSPercent();
                cPercent += pastUserDisc.getCPercent();

                int result = teamDao.updateUserDisc(pastUserDisc.getUserDiscIdx(), x, y, dPercent, iPercent, sPercent, cPercent);
                if (result == 0){
                    throw new BaseException(MODIFY_FAIL_USERDISC);
                }
            }

            // 평가 기록 저장
            int reviewIdx = teamDao.createReview(reviewerIdx, reviewingIdx, postReviewReq, reviewDisc);
            return new PostReviewRes(reviewIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
