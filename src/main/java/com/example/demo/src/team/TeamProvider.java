package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.src.team.model.GetCommentsRes;
import com.example.demo.src.team.model.GetTeamRes;
import com.example.demo.src.team.model.GetTeamsRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_TEAMIDX;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamProvider {

    private final TeamDao teamDao;

    public int checkUserIdx(int userIdx) throws BaseException {
        try {
            return teamDao.checkUserIdx(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamIdxByLeader(int teamIdx, int leaderIdx) throws BaseException {
        try {
            return teamDao.checkTeamIdxByLeader(teamIdx, leaderIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPreTeamMember(int teamIdx, int userIdx) throws BaseException {
        try {
            return teamDao.checkPreTeamMember(teamIdx, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTeamsRes> getTeams(int userIdx) throws BaseException {
        try {
            List<GetTeamsRes> getTeamsResList = teamDao.getTeams(userIdx);
            return getTeamsResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamIdx(int teamIdx) throws BaseException {
        try {
            return teamDao.checkTeamIdx(teamIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTeamRes> getTeam(int teamIdx) throws BaseException {
        // 유효한 팀 인덱스인지, 삭제된 팀이 아닌지 확인
        if(checkTeamIdx(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx);
            return getTeamRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCommentsRes> getComments(int teamIdx) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(checkTeamIdx(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            List<GetCommentsRes> getCommentsResList = teamDao.getComments(teamIdx);
            return getCommentsResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
