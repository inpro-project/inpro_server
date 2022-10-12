package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.GetLikerRes;
import com.example.demo.src.match.model.GetLikingRes;
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

    public List<GetLikingRes> getLikings(int userIdx) throws BaseException {
        try {
            List<GetLikingRes> getLikingResList = matchDao.getLikings(userIdx);
            return getLikingResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetLikerRes> getLikers(int userIdx) throws BaseException {
        try {
            List<GetLikerRes> getLikerResList = matchDao.getLikers(userIdx);
            return getLikerResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
