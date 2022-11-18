package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetPortfolioRes;
import com.example.demo.src.user.model.GetUserProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INACTIVE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProvider {

    private final UserDao userDao;

    public int checkPortfolioIdx(int userIdx, int portfolioIdx) throws BaseException {
        try {
            return userDao.checkPortfolioIdx(userIdx, portfolioIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserIdx(int userIdx) throws BaseException {
        try {
            return userDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPortfolioRes> getPortfolios(int userIdx, int portfolioCategoryIdx) throws BaseException {
        // userIdx 유효성 검사
        if(checkUserIdx(userIdx) != 1){
            throw new BaseException(INACTIVE_USER);
        }
        try {
            List<GetPortfolioRes> getPortfolioRes = userDao.getPortfolios(userIdx, portfolioCategoryIdx);
            return getPortfolioRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getNumOfUserTag(int userIdx) throws BaseException {
        try {
            return userDao.getNumOfUserTag(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserTagIdx(int userIdx, int userTagIdx) throws BaseException {
        try {
            return userDao.checkUserTagIdx(userIdx, userTagIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserProfileRes getUserProfile(int userIdx) throws BaseException {
        try{
            GetUserProfileRes getUserProfileRes = userDao.getUserProfile(userIdx);
            return getUserProfileRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPortfolio(int userIdx, int portfolioCategoryIdx) throws BaseException {
        try {
            return userDao.checkPortfolio(userIdx, portfolioCategoryIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
