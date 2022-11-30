package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.src.team.model.*;
import com.example.demo.src.user.model.UserDisc;
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

    public List<GetTeamRes> getTeam(int teamIdx, int userIdx) throws BaseException {
        // 유효한 팀 인덱스인지, 삭제된 팀이 아닌지 확인
        if(checkTeamDeleted(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            // 팀의 리더의 search disc나 유저의 user disc 중에 하나라도 없어도 0
            if(teamDao.checkSearchDiscByTeamIdx(teamIdx) == 0 || teamDao.checkUserDisc(userIdx) == 0){
                SearchDiscAndPercent searchDiscAndPercent = new SearchDiscAndPercent(0.0, 0.0, 0);
                List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx, searchDiscAndPercent);
                return getTeamRes;
            }
            else {
                // 현재 로그인한 유저의 user disc
                UserDiscXy userDiscXy = teamDao.getUserDiscXy(userIdx);

                // 리더의 search disc와 현재 로그인한 유저와의 일치 퍼센트
                int leaderIdx = teamDao.getLeaderIdx(teamIdx);
                SearchDiscAndPercent searchDiscAndPercent = teamDao.getSearchDiscAndPercent(leaderIdx, userDiscXy);

                List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx, searchDiscAndPercent);
                return getTeamRes;
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCommentsRes> getComments(int teamIdx) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(checkTeamDeleted(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            List<GetCommentsRes> getCommentsResList = teamDao.getComments(teamIdx);
            return getCommentsResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkCommentIdx(int commentIdx) throws BaseException {
        try {
            return teamDao.checkCommentIdx(commentIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkCommentByUserIdx(int commentIdx, int userIdx) throws BaseException {
        try {
            return teamDao.checkCommentByUserIdx(commentIdx, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamActive(int teamIdx) throws BaseException {
        try {
            return teamDao.checkTeamActive(teamIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamInActive(int teamIdx) throws BaseException {
        try {
            return teamDao.checkTeamInActive(teamIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTeamDeleted(int teamIdx) throws BaseException {
        try {
            return teamDao.checkTeamDeleted(teamIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTeamImgsRes> getTeamImgs(int teamIdx) throws BaseException {
        // 유효한 팀 인덱스인지 확인
        if(checkTeamDeleted(teamIdx) == 0){
            throw new BaseException(INVALID_TEAMIDX);
        }

        try {
            List<GetTeamImgsRes> getTeamImgsResList = teamDao.getTeamImgs(teamIdx);
            return getTeamImgsResList;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
