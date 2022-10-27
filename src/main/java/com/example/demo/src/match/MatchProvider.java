package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.*;
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
public class MatchProvider {

    private final MatchDao matchDao;

    public int checkUserIdx(int userIdx) throws BaseException {
        try {
            return matchDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreUserLike(int likerIdx, int likingIdx) throws BaseException {
        try {
            return matchDao.checkPreUserLike(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserLikeHist(int likerIdx, int likingIdx) throws BaseException {
        try {
            return matchDao.checkUserLikeHist(likerIdx, likingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreUserPass(int passerIdx, int passingIdx) throws BaseException {
        try {
            return matchDao.checkPreUserPass(passerIdx, passingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserPassHist(int passerIdx, int passingIdx) throws BaseException {
        try {
            return matchDao.checkUserPassHist(passerIdx, passingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserLikingRes> getUserLikings(int userIdx) throws BaseException {
        try {
            List<GetUserLikingRes> getUserLikingResList = matchDao.getUserLikings(userIdx);
            return getUserLikingResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserLikerRes> getUserLikers(int userIdx) throws BaseException {
        try {
            List<GetUserLikerRes> getUserLikerResList = matchDao.getUserLikers(userIdx);
            return getUserLikerResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMatchedUserRes> getMatchedUsers(int userIdx) throws BaseException{
        try {
            List<GetMatchedUserRes> getMatchedUserResList = matchDao.getMatchedUsers(userIdx);
            return getMatchedUserResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserFilterRes> getUserFilters(int userIdx) throws BaseException {
        try {
            List<GetUserFilterRes> getUserFilterResList = matchDao.getUserFilters(userIdx);
            return getUserFilterResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserFilterByName(int userIdx, String name) throws BaseException {
        try {
            return matchDao.checkUserFilterByName(userIdx, name);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserFilterByIdx(int userIdx, int userFilterIdx) throws BaseException {
        try {
            return matchDao.checkUserFilterByIdx(userIdx, userFilterIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProjectFilterRes> getProjectFilters(int userIdx) throws BaseException {
        try {
            List<GetProjectFilterRes> getProjectFilterResList = matchDao.getProjectFilters(userIdx);
            return getProjectFilterResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
