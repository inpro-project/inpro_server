package com.example.demo.src.team;

import com.example.demo.config.BaseException;
import com.example.demo.src.team.model.*;
import com.example.demo.src.team.model.GetTeamMatchRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
                if(teamDao.checkUserDisc(userIdx) == 0){
                    List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx, userIdx, searchDiscAndPercent, 0.0, 0.0);
                    return getTeamRes;
                }
                UserDiscXy userDiscXy = teamDao.getUserDiscXy(userIdx);
                List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx, userIdx, searchDiscAndPercent, userDiscXy.getX(), userDiscXy.getY());
                return getTeamRes;
            }
            else {
                // 현재 로그인한 유저의 user disc
                UserDiscXy userDiscXy = teamDao.getUserDiscXy(userIdx);

                // 리더의 search disc와 현재 로그인한 유저와의 일치 퍼센트
                int leaderIdx = teamDao.getLeaderIdx(teamIdx);
                SearchDiscAndPercent searchDiscAndPercent = teamDao.getSearchDiscAndPercent(leaderIdx, userDiscXy);

                List<GetTeamRes> getTeamRes = teamDao.getTeam(teamIdx, userIdx, searchDiscAndPercent, userDiscXy.getX(), userDiscXy.getY());
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

    public int checkTeamFinish(int teamIdx) throws BaseException {
        try {
            return teamDao.checkTeamFinish(teamIdx);
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

    public List<GetReviewRes> getReview() throws BaseException {
        try {
            List<GetReviewRes> getReviewRes = teamDao.getReview();
            return getReviewRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPastReview(int teamIdx, int reviewerIdx, int reviewingIdx) throws BaseException {
        try {
            return teamDao.checkPastReview(teamIdx, reviewerIdx, reviewingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PastUserDisc getPastUserDisc(int userIdx) throws BaseException {
        try {
            PastUserDisc pastUserDisc = teamDao.getPastUserDisc(userIdx);
            return pastUserDisc;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTeamMatchRes> getTeamMatches(int userIdx) throws BaseException {
        try {
            // 1 : 팀 유형
            List<String> getTypeFilter = teamDao.getTeamFilter(userIdx, 1);
            int typeSize = 6 - getTypeFilter.size();
            if(getTypeFilter.get(0).equals("무관")){
                getTypeFilter = Arrays.asList(new String[]{"프로젝트", "스터디", "대외활동", "창업", "공모전", "동아리"});
            }
            else{
                for(int i = 0; i < typeSize; i++){
                    getTypeFilter.add(null);
                }
            }

            // 2 : 지역
            List<String> getRegionFilter = teamDao.getTeamFilter(userIdx, 2);
            int regionSize = 17 - getRegionFilter.size();
            if(getRegionFilter.get(0).equals("무관")){
                getRegionFilter = Arrays.asList(new String[]{"서울", "인천", "경기", "강원", "충북", "충남", "세종", "대전", "전북", "전남", "광주", "경북", "경남", "대구", "울산", "부산", "제주"});;
            }
            else{
                for(int i = 0; i < regionSize; i++){
                    getRegionFilter.add(null);
                }
            }

            // 3 : 분야
            List<String> getInterestsFilter = teamDao.getTeamFilter(userIdx, 3);
            int interestsSize = 14 - getInterestsFilter.size();
            if(getInterestsFilter.get(0).equals("무관")){
                getInterestsFilter = Arrays.asList(new String[]{"경영/사무", "마케팅/광고", "IT/인터넷", "디자인", "무역/유통", "영업/고객상담", "서비스", "연구개발/설계", "생산/제조", "교육", "건설", "의료", "미디어", "전문/특수직"});
            }
            else {
                for(int i = 0; i < interestsSize; i++){
                    getInterestsFilter.add(null);
                }
            }

            // 현재 로그인한 유저의 user disc
            UserDiscXy userDiscXy = teamDao.getUserDiscXy(userIdx);

            List<GetTeamMatchRes> getTeamMatchRes = teamDao.getTeamMatches(userDiscXy, userIdx, getTypeFilter, getRegionFilter, getInterestsFilter);
            return getTeamMatchRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
