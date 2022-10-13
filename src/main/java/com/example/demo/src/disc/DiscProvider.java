package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.src.disc.model.GetDiscTestRes;
import com.example.demo.src.disc.model.GetSearchDiscResultRes;
import com.example.demo.src.disc.model.GetUserDiscResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscProvider {

    private final DiscDao discDao;

    public List<GetDiscTestRes> getDiscTest() throws BaseException {
        try {
            List<GetDiscTestRes> getDiscTestRes = discDao.getDiscTest();
            return getDiscTestRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserDisc(int userIdx) throws BaseException {
        try {
            return discDao.checkUserDisc(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkSearchDisc(int userIdx) throws BaseException {
        try {
            return discDao.checkSearchDisc(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserDiscIdx(int userIdx, int userDiscIdx) throws BaseException {
        try {
            return discDao.checkUserDiscIdx(userIdx, userDiscIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserDiscResultRes getUserDiscResult(int userIdx, int userDiscIdx) throws BaseException {
        // userDiscIdx 유효성 검사
        if(checkUserDiscIdx(userIdx, userDiscIdx) != 1){
            throw new BaseException(USERDISC_INVALID_USERDISCIDX);
        }
        try {
            GetUserDiscResultRes getDiscResultRes = discDao.getUserDiscResult(userDiscIdx);
            return getDiscResultRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkSearchDiscIdx(int userIdx, int searchDiscIdx) throws BaseException {
        try {
            return discDao.checkSearchDiscIdx(userIdx, searchDiscIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSearchDiscResultRes getSearchDiscResult(int userIdx, int searchDiscIdx) throws BaseException {
        // searchDiscIdx 유효성 검사
        if(checkSearchDiscIdx(userIdx, searchDiscIdx) != 1){
            throw new BaseException(SEARCHDISC_INVALID_SEARCHDISCIDX);
        }
        try {
            GetSearchDiscResultRes getSearchDiscResultRes = discDao.getSearchDiscResult(searchDiscIdx);
            return getSearchDiscResultRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getUserDiscCount(int userIdx) throws BaseException {
        try {
            return discDao.getUserDiscCount(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getSearchDiscCount(int userIdx) throws BaseException {
        try {
            return discDao.getSearchDiscCount(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserDiscResultRes> getUserDiscResultList(int userIdx) throws BaseException {
        try {
            List<GetUserDiscResultRes> getUserDiscResultRes = discDao.getUserDiscResultList(userIdx);
            return getUserDiscResultRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSearchDiscResultRes> getSearchDiscResultList(int userIdx) throws BaseException {
        try {
            List<GetSearchDiscResultRes> getSearchDiscResultRes = discDao.getSearchDiscResultList(userIdx);
            return getSearchDiscResultRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
