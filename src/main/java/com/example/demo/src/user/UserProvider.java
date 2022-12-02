package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.SearchDiscXy;
import com.example.demo.src.user.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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

    public int checkUserDisc(int userIdx) throws BaseException {
        try {
            return userDao.checkUserDisc(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public UserDisc getUserDisc(int userIdx) throws BaseException {
        try {
            UserDisc userDisc = userDao.getUserDisc(userIdx);
            return userDisc;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMyProfileRes getMyProfile(int userIdx) throws BaseException {
        try{
            if(checkUserDisc(userIdx) == 0){
                double x = 0.0;
                double y = 0.0;
                GetMyProfileRes getMyProfileRes = userDao.getMyProfile(userIdx, x, y);
                return getMyProfileRes;
            }
            else {
                UserDisc userDisc = getUserDisc(userIdx);
                GetMyProfileRes getMyProfileRes = userDao.getMyProfile(userIdx, userDisc.getX(), userDisc.getY());
                return getMyProfileRes;
            }

        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserProfileRes getUserProfile(int loginUserIdx, int userIdx) throws BaseException {
        if(checkUserIdx(userIdx) == 0){
            throw new BaseException(INVALID_USERIDX);
        }
        try{
            // 현재 로그인한 유저의 업무 성향 검사 내역이 없다면
            if(checkUserDisc(loginUserIdx) == 0){
                double x = 0.0;
                double y = 0.0;
                GetUserProfileRes getUserProfileRes = userDao.getUserProfile(x, y, userIdx);
                return getUserProfileRes;
            }
            else {
                UserDisc userDisc = getUserDisc(loginUserIdx);
                GetUserProfileRes getUserProfileRes = userDao.getUserProfile(userDisc.getX(), userDisc.getY(), userIdx);
                return getUserProfileRes;
            }
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

    public int checkSearchDisc(int userIdx) throws BaseException {
        try {
            return userDao.checkSearchDisc(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SearchDiscXy getPastSearchDisc(int userIdx) throws BaseException {
        try {
            SearchDiscXy searchDiscXy = userDao.getPastSearchDisc(userIdx);
            return searchDiscXy;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SearchDiscXy getNewSearchDisc(int userIdx) throws BaseException {
        try {
            SearchDiscXy searchDiscXy = userDao.getNewSearchDisc(userIdx);
            return searchDiscXy;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserMatchRes> getUserMatches(int userIdx, SearchDiscXy newSearchDisc) throws BaseException {
        try {
            // 1 : 연령대
            List<String> getAgeRangeFilter = userDao.getUserFilter(userIdx, 1);
            int ageRangeSize = 6 - getAgeRangeFilter.size();
            if(getAgeRangeFilter.get(0).equals("무관")){
                getAgeRangeFilter = Arrays.asList(new String[]{"10대", "20대", "30대", "40대", "50대", "60대"});
            }
            else{
                for(int i = 0; i < ageRangeSize; i++){
                    getAgeRangeFilter.add(null);
                }
            }

            // 2 : 지역
            List<String> getRegionFilter = userDao.getUserFilter(userIdx, 2);
            int regionSize = 17 - getRegionFilter.size();
            if(getRegionFilter.get(0).equals("무관")){
                getRegionFilter = Arrays.asList(new String[]{"서울", "인천", "경기", "강원", "충북", "충남", "세종", "대전", "전북", "전남", "광주", "경북", "경남", "대구", "울산", "부산", "제주"});;
            }
            else{
                for(int i = 0; i < regionSize; i++){
                    getRegionFilter.add(null);
                }
            }

            // 3 : 직업군
            List<String> getOccupationFilter = userDao.getUserFilter(userIdx, 3);
            int occupationSize = 5 - getOccupationFilter.size();
            if(getOccupationFilter.get(0).equals("무관")){
                getOccupationFilter = Arrays.asList(new String[]{"학생", "대학원생", "직장인", "취준생", "자영업자"});
            }
            else{
                for(int i = 0; i < occupationSize; i++){
                    getOccupationFilter.add(null);
                }
            }

            // 4 : 분야
            List<String> getInterestsFilter = userDao.getUserFilter(userIdx, 4);
            int interestsSize = 14 - getInterestsFilter.size();
            if(getInterestsFilter.get(0).equals("무관")){
                getInterestsFilter = Arrays.asList(new String[]{"경영/사무", "마케팅/광고", "IT/인터넷", "디자인", "무역/유통", "영업/고객상담", "서비스", "연구개발/설계", "생산/제조", "교육", "건설", "의료", "미디어", "전문/특수직"});
            }
            else {
                for(int i = 0; i < interestsSize; i++){
                    getInterestsFilter.add(null);
                }
            }

            List<GetUserMatchRes> getUserMatchRes = userDao.getUserMatches(userIdx, newSearchDisc
                    , getAgeRangeFilter, getRegionFilter, getOccupationFilter, getInterestsFilter);
            return getUserMatchRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
