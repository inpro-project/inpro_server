package com.example.demo.src.disc;

import com.example.demo.config.BaseException;
import com.example.demo.src.disc.model.GetDiscResultRes;
import com.example.demo.src.disc.model.GetDiscTestRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.GET_USERDISC_INVALID_USERDISCIDX;

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

    public GetDiscResultRes getUserDiscResult(int userDiscIdx) throws BaseException {
        try {
            GetDiscResultRes getDiscResultRes = discDao.getUserDiscResult(userDiscIdx);
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

    public GetDiscResultRes getSearchDiscResult(int searchDiscIdx) throws BaseException {
        try {
            GetDiscResultRes getDiscResultRes = discDao.getSearchDiscResult(searchDiscIdx);
            return getDiscResultRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
