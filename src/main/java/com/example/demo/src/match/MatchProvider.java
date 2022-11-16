package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchProvider {

    private final MatchDao matchDao;

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

    public List<GetTeamFilterRes> getTeamFilters(int userIdx) throws BaseException {
        try {
            List<GetTeamFilterRes> getTeamFilterResList = matchDao.getTeamFilters(userIdx);
            return getTeamFilterResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamFilterByName(int userIdx, String name) throws BaseException {
        try {
            return matchDao.checkTeamFilterByName(userIdx, name);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamFilterByIdx(int userIdx, int teamFilterIdx) throws BaseException {
        try {
            return matchDao.checkTeamFilterByIdx(userIdx, teamFilterIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DiscXy getUserDiscXy(int userIdx) throws BaseException {
        try {
            DiscXy discXy = matchDao.getUserDiscXy(userIdx);
            return discXy;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserMatchRes> getUserMatches(int userIdx) throws BaseException {
        try {
            // 1 : 연령대
            List<String> getAgeRangeFilter = matchDao.getUserFilter(userIdx, 1);
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
            List<String> getRegionFilter = matchDao.getUserFilter(userIdx, 2);
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
            List<String> getOccupationFilter = matchDao.getUserFilter(userIdx, 3);
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
            List<String> getInterestsFilter = matchDao.getUserFilter(userIdx, 4);
            int interestsSize = 14 - getInterestsFilter.size();
            if(getInterestsFilter.get(0).equals("무관")){
                getInterestsFilter = Arrays.asList(new String[]{"경영/사무", "마케팅/광고/홍보", "IT/인터넷", "디자인", "무역/유통", "영업/고객상담", "서비스", "연구개발/설계", "생산/제조", "교육", "건설", "의료", "미디어", "전문/특수직"});
            }
            else {
                for(int i = 0; i < interestsSize; i++){
                    getInterestsFilter.add(null);
                }
            }

            List<GetUserMatchRes> getUserMatchRes = matchDao.getUserMatches(userIdx, getUserDiscXy(userIdx)
                    , getAgeRangeFilter, getRegionFilter, getOccupationFilter, getInterestsFilter);
            return getUserMatchRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMatchedTeamRes> getMatchedTeams(int userIdx) throws BaseException {
        try {
            List<GetMatchedTeamRes> getMatchedTeamResList = matchDao.getMatchedTeams(userIdx);
            return getMatchedTeamResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
