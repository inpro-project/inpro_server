package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetUserLikerRes;
import com.example.demo.src.like.model.GetUserLikingRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeProvider {

    private final LikeDao likeDao;

    public int checkUserIdx(int userIdx) throws BaseException {
        try {
            return likeDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreUserLike(int likerIdx, int likingIdx) throws BaseException {
        try {
            return likeDao.checkPreUserLike(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserLikeHist(int likerIdx, int likingIdx) throws BaseException {
        try {
            return likeDao.checkUserLikeHist(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreUserPass(int passerIdx, int passingIdx) throws BaseException {
        try {
            return likeDao.checkPreUserPass(passerIdx, passingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserPassHist(int passerIdx, int passingIdx) throws BaseException {
        try {
            return likeDao.checkUserPassHist(passerIdx, passingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserLikingRes> getUserLikings(int userIdx) throws BaseException {
        try {
            List<GetUserLikingRes> getUserLikingResList = likeDao.getUserLikings(userIdx);
            return getUserLikingResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserLikerRes> getUserLikers(int userIdx) throws BaseException {
        try {
            List<GetUserLikerRes> getUserLikerResList = likeDao.getUserLikers(userIdx);
            return getUserLikerResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamIdx(int teamIdx) throws BaseException {
        try {
            return likeDao.checkTeamIdx(teamIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreTeamLike(int likerIdx, int likingIdx) throws BaseException {
        try {
            return likeDao.checkPreTeamLike(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamLikeHist(int likerIdx, int likingIdx) throws BaseException {
        try {
            return likeDao.checkTeamLikeHist(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
