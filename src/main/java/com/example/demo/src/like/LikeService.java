package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.PostTeamLikeRes;
import com.example.demo.src.like.model.PostUserLikeRes;
import com.example.demo.src.like.model.PostUserPassRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LikeService {

    private final LikeProvider likeProvider;
    private final LikeDao likeDao;

    public void updateUserLike(int userLikeIdx) throws BaseException {
        try {
            int result = likeDao.updateUserLike(userLikeIdx);
            if(result == 0){
                throw new BaseException(FAIL_USERLIKE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserLikeRes createUserLike(int likerIdx, int likingIdx) throws BaseException {
        // likingIdx 유효성 검사
        if(likeProvider.checkUserIdx(likingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 중복 좋아요 유효성 검사
        if(likeProvider.checkPreUserLike(likerIdx, likingIdx) == 1){
            throw new BaseException(USERLIKE_INVALID_LIKINGIDX);
        }

        try {
            // 이전에 비활성화나 좋아요 취소를 했던 유저의 경우
            int preUserLikeIdx = likeProvider.checkUserLikeHist(likerIdx, likingIdx);
            if(preUserLikeIdx != 0){
                // active 업데이트
                updateUserLike(preUserLikeIdx);
                return new PostUserLikeRes(preUserLikeIdx);
            }

            int userLikeIdx = likeDao.createUserLike(likerIdx, likingIdx);
            return new PostUserLikeRes(userLikeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUserLike(int likerIdx, int likingIdx) throws BaseException {
        // likingIdx 유효성 검사
        if(likeProvider.checkUserIdx(likingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 좋아요 누르지 않은 유저에 대해 좋아요 취소 요청을 할 경우
        if(likeProvider.checkPreUserLike(likerIdx, likingIdx) == 0){
            throw new BaseException(UNUSERLIKE_INVALID_LIKINGIDX);
        }

        try {
            int result = likeDao.deleteUserLike(likerIdx, likingIdx);
            if(result == 0){
                throw new BaseException(FAIL_UNUSERLIKE);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateUserPass(int userPassIdx) throws BaseException {
        try {
            int result = likeDao.updateUserPass(userPassIdx);
            if(result == 0){
                throw new BaseException(FAIL_USERPASS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostUserPassRes createUserPass(int passerIdx, int passingIdx) throws BaseException {
        // passingIdx 유효성 검사
        if(likeProvider.checkUserIdx(passingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 중복 넘기기 유효성 검사
        if(likeProvider.checkPreUserPass(passerIdx, passingIdx) == 1){
            throw new BaseException(USERPASS_INVALID_PASSINGIDX);
        }

        try {
            // 이전에 비활성화나 넘기기 취소를 했던 유저의 경우
            int preUserPassIdx = likeProvider.checkUserPassHist(passerIdx, passingIdx);
            if(preUserPassIdx != 0){
                // active 업데이트
                updateUserPass(preUserPassIdx);
                return new PostUserPassRes(preUserPassIdx);
            }

            int userPassIdx = likeDao.createUserPass(passerIdx, passingIdx);
            return new PostUserPassRes(userPassIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUserPass(int passerIdx, int passingIdx) throws BaseException {
        // passingIdx 유효성 검사
        if(likeProvider.checkUserIdx(passingIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }

        // 넘기기를 누르지 않은 유저에 대해 넘기기 취소 요청을 할 경우
        if(likeProvider.checkPreUserPass(passerIdx, passingIdx) == 0){
            throw new BaseException(UNUSERPASS_INVALID_PASSINGIDX);
        }

        try {
            int result = likeDao.deleteUserPass(passerIdx, passingIdx);
            if(result == 0){
                throw new BaseException(FAIL_UNUSERPASS);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateTeamLike(int teamLikeIdx) throws BaseException {
        try {
            int result = likeDao.updateTeamLike(teamLikeIdx);
            if(result == 0){
                throw new BaseException(FAIL_TEAMLIKE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostTeamLikeRes createTeamLike(int likerIdx, int likingIdx) throws BaseException {
        // likingIdx 유효성 검사
        if(likeProvider.checkTeamIdx(likingIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        // 중복 좋아요 유효성 검사
        if(likeProvider.checkPreTeamLike(likerIdx, likingIdx) == 1){
            throw new BaseException(TEAMLIKE_INVALID_LIKINGIDX);
        }

        try {
            // 이전에 비활성화나 좋아요 취소를 했던 팀의 경우
            int preTeamLikeIdx = likeProvider.checkTeamLikeHist(likerIdx, likingIdx);
            if(preTeamLikeIdx != 0){
                // active 업데이트
                updateTeamLike(preTeamLikeIdx);
                return new PostTeamLikeRes(preTeamLikeIdx);
            }

            int teamLikeIdx = likeDao.createTeamLike(likerIdx, likingIdx);
            return new PostTeamLikeRes(teamLikeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
