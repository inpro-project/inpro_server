package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import com.example.demo.src.match.model.*;
import com.example.demo.src.user.model.GetUserMatchRes;
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

    public List<GetMatchedTeamRes> getMatchedTeams(int userIdx) throws BaseException {
        try {
            List<GetMatchedTeamRes> getMatchedTeamResList = matchDao.getMatchedTeams(userIdx);
            return getMatchedTeamResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
