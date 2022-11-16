package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    public int checkTeamIdx(int teamIdx, int leaderIdx) throws BaseException {
        try {
            return teamDao.checkTeamIdx(teamIdx, leaderIdx);
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

}