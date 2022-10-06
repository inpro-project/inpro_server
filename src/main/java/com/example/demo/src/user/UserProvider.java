package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetPortfolioRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    public List<GetPortfolioRes> getMyPortfolios(int userIdx, int portfolioCategoryIdx) throws BaseException {
        try {
            List<GetPortfolioRes> getPortfolioRes = userDao.getMyPortfolios(userIdx, portfolioCategoryIdx);
            return getPortfolioRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
