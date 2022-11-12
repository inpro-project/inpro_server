package com.example.demo.src.match;

import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MatchService {

    private final MatchProvider matchProvider;
    private final MatchDao matchDao;

    public void createAgeRangeFilter(int userIdx, List<String> ageRange) throws BaseException {
        try {
            for(int i = 0; i < ageRange.size(); i++){
                matchDao.createAgeRangeFilter(userIdx, ageRange.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createRegionFilter(int userIdx, List<String> region) throws BaseException {
        try {
            for(int i = 0; i < region.size(); i++){
                matchDao.createRegionFilter(userIdx, region.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createOccupationFilter(int userIdx, List<String> occupation) throws BaseException {
        try {
            for(int i = 0; i < occupation.size(); i++){
                matchDao.createOccupationFilter(userIdx, occupation.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createInterestsFilter(int userIdx, List<String> interests) throws BaseException {
        try {
            for(int i = 0; i < interests.size(); i++){
                matchDao.createInterestsFilter(userIdx, interests.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateAgeRangeFilter(int userIdx, List<String> ageRange) throws BaseException {
        try {
            int userFilterIdx = 0;
            for(int i = 0; i < ageRange.size(); i++){
                // 기존 존재 여부 확인
                userFilterIdx = matchProvider.checkUserFilterByName(userIdx, ageRange.get(i));
                // 존재하지 않으면 새로 추가
                if(userFilterIdx == 0){
                    matchDao.createAgeRangeFilter(userIdx, ageRange.get(i));
                }
                // 이전에 추가됐던 것이라면 status만 업데이트
                else {
                    matchDao.updateUserFilter(userIdx, userFilterIdx);
                }

            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateRegionFilter(int userIdx, List<String> region) throws BaseException {
        try {
            int userFilterIdx = 0;
            for(int i = 0; i < region.size(); i++){
                userFilterIdx = matchProvider.checkUserFilterByName(userIdx, region.get(i));
                if(userFilterIdx == 0){
                    matchDao.createRegionFilter(userIdx, region.get(i));
                }
                matchDao.updateUserFilter(userIdx, userFilterIdx);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateOccupationFilter(int userIdx, List<String> occupation) throws BaseException {
        try {
            int userFilterIdx = 0;
            for(int i = 0; i < occupation.size(); i++){
                userFilterIdx = matchProvider.checkUserFilterByName(userIdx, occupation.get(i));
                if(userFilterIdx == 0) {
                    matchDao.createOccupationFilter(userIdx, occupation.get(i));
                }
                matchDao.updateUserFilter(userIdx, userFilterIdx);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateInterestsFilter(int userIdx, List<String> interests) throws BaseException {
        try {
            int userFilterIdx = 0;
            for(int i = 0; i < interests.size(); i++){
                userFilterIdx = matchProvider.checkUserFilterByName(userIdx, interests.get(i));
                if(userFilterIdx == 0) {
                    matchDao.createInterestsFilter(userIdx, interests.get(i));
                }
                matchDao.updateUserFilter(userIdx, userFilterIdx);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUserFilter(int userIdx, List<Integer> userFilterIdxlist) throws BaseException {
        try {
            for(int i = 0; i < userFilterIdxlist.size(); i++){
                // 존재하는 active userFilterIdx인 경우에만 삭제 가능
                if(matchProvider.checkUserFilterByIdx(userIdx, userFilterIdxlist.get(i)) == 1){
                    matchDao.deleteUserFilter(userIdx, userFilterIdxlist.get(i));
                }
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamTypeFilter(int userIdx, List<String> type) throws BaseException {
        try {
            for(int i = 0; i < type.size(); i++){
                matchDao.createTeamTypeFilter(userIdx, type.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamRegionFilter(int userIdx, List<String> region) throws BaseException {
        try {
            for(int i = 0; i < region.size(); i++){
                matchDao.createTeamRegionFilter(userIdx, region.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createTeamInterestsFilter(int userIdx, List<String> interests) throws BaseException {
        try {
            for(int i = 0; i < interests.size(); i++){
                matchDao.createTeamInterestsFilter(userIdx, interests.get(i));
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateTeamTypeFilter(int userIdx, List<String> type) throws BaseException {
        try {
            int teamFilterIdx = 0;
            for(int i = 0; i < type.size(); i++){
                // 기존 존재 여부 확인
                teamFilterIdx = matchProvider.checkTeamFilterByName(userIdx, type.get(i));
                // 존재하지 않으면 새로 추가
                if(teamFilterIdx == 0){
                    matchDao.createTeamTypeFilter(userIdx, type.get(i));
                }
                // 이전에 추가됐던 것이라면 status만 업데이트
                else {
                    matchDao.updateTeamFilter(userIdx, teamFilterIdx);
                }

            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateTeamRegionFilter(int userIdx, List<String> region) throws BaseException {
        try {
            int teamFilterIdx = 0;
            for(int i = 0; i < region.size(); i++){
                teamFilterIdx = matchProvider.checkTeamFilterByName(userIdx, region.get(i));
                if(teamFilterIdx == 0){
                    matchDao.createTeamRegionFilter(userIdx, region.get(i));
                }
                matchDao.updateTeamFilter(userIdx, teamFilterIdx);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateTeamInterestsFilter(int userIdx, List<String> interests) throws BaseException {
        try {
            int teamFilterIdx = 0;
            for(int i = 0; i < interests.size(); i++){
                teamFilterIdx = matchProvider.checkTeamFilterByName(userIdx, interests.get(i));
                if(teamFilterIdx == 0) {
                    matchDao.createTeamInterestsFilter(userIdx, interests.get(i));
                }
                matchDao.updateTeamFilter(userIdx, teamFilterIdx);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteTeamFilter(int userIdx, List<Integer> teamFilterIdxlist) throws BaseException {
        try {
            for(int i = 0; i < teamFilterIdxlist.size(); i++){
                // 존재하는 active teamFilterIdx인 경우에만 삭제 가능
                if(matchProvider.checkTeamFilterByIdx(userIdx, teamFilterIdxlist.get(i)) == 1){
                    matchDao.deleteTeamFilter(userIdx, teamFilterIdxlist.get(i));
                }
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
