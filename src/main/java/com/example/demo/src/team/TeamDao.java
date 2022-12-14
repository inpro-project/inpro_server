package com.example.demo.src.team;

import com.example.demo.src.team.model.*;
import com.example.demo.src.team.model.GetTeamMatchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TeamDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createTeam(int userIdx, int chatRoomIdx, PostTeamReq postTeamReq) {
        String createTeamQuery = "insert into Team (userIdx, title, content, type, region, interests, chatRoomIdx) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] createTeamParams = new Object[]{userIdx, postTeamReq.getTitle(), postTeamReq.getContent(), postTeamReq.getType(), postTeamReq.getRegion(), postTeamReq.getInterests(), chatRoomIdx};
        this.jdbcTemplate.update(createTeamQuery, createTeamParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createTeamLeader(int teamIdx, int userIdx) {
        String createTeamLeaderQuery = "insert into TeamMember (teamIdx, userIdx, role) VALUES (?, ?, '리더')";
        Object[] createTeamLeaderParams = new Object[]{teamIdx, userIdx};
        this.jdbcTemplate.update(createTeamLeaderQuery, createTeamLeaderParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createTeamImg(int teamIdx, String fileName, String teamImgUrl, String isRepImg) {
        String createTeamImgQuery = "insert into TeamFile (teamIdx, type, fileName, teamFileUrl, isRepImg) VALUES (?, 'Y', ?, ?, ?)";
        Object[] createTeamImgParams = new Object[]{teamIdx, fileName, teamImgUrl, isRepImg};

        return this.jdbcTemplate.update(createTeamImgQuery, createTeamImgParams);
    }

    public int createTeamFile(int teamIdx, String fileName, String teamFileUrl) {
        String createTeamFileQuery = "insert into TeamFile (teamIdx, type, fileName, teamFileUrl, isRepImg) VALUES (?, 'N', ?, ?, 'N')";
        Object[] createTeamFileParams = new Object[]{teamIdx, fileName, teamFileUrl};

        return this.jdbcTemplate.update(createTeamFileQuery, createTeamFileParams);
    }

    public int checkUserIdx(int userIdx){
        String checkUserIdxQuery = "select exists(select * from User where userIdx = ? and status = 'active')";
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkUserIdxParams);
    }

    public int checkTeamIdxByLeader(int teamIdx, int leaderIdx){
        String checkTeamIdxByLeaderQuery = "select exists(select * from TeamMember where teamIdx = ? and userIdx = ? and role = '리더' and status = 'active')";
        Object[] checkTeamIdxByLeaderParams = new Object[]{teamIdx, leaderIdx};
        return this.jdbcTemplate.queryForObject(checkTeamIdxByLeaderQuery, int.class, checkTeamIdxByLeaderParams);
    }

    public int checkPreTeamMember(int teamIdx, int userIdx){
        String checkPreTeamMemberQuery = "select exists(select * from TeamMember where teamIdx = ? and userIdx = ?)";
        Object[] checkPreTeamMemberParams = new Object[]{teamIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkPreTeamMemberQuery, int.class, checkPreTeamMemberParams);
    }

    public int createMember(PostMemberReq postMemberReq) {
        String createMemberQuery = "insert into TeamMember (teamIdx, userIdx, role) VALUES (?, ?, ?)";
        Object[] createMemberParams = new Object[]{postMemberReq.getTeamIdx(), postMemberReq.getUserIdx(), postMemberReq.getRole()};
        this.jdbcTemplate.update(createMemberQuery, createMemberParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetTeamsRes> getTeams(int userIdx){
        String getTeamsQuery = "select teamIdx\n" +
                "     , (select case when count(*) = 0 then 0 else teamFileUrl end as teamRepUrl\n" +
                "        from TeamFile\n" +
                "        where TeamFile.teamIdx = teamIdx and TeamFile.status = 'active' and isRepImg = 'Y') as teamRepUrl\n" +
                "    , title, type, region, interests, status\n" +
                "from Team\n" +
                "where userIdx = ? and status != 'deleted' order by createdAt DESC";
        int getTeamsParams = userIdx;
        return this.jdbcTemplate.query(getTeamsQuery,
                (rs, rsNum) -> new GetTeamsRes(
                        rs.getInt("teamIdx"),
                        rs.getString("teamRepUrl"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests"),
                        rs.getString("status")),
                getTeamsParams);
    }

    public int checkTeamActive(int teamIdx){
        String checkTeamActiveQuery = "select exists(select * from Team where teamIdx = ? and status = 'active')";
        int checkTeamActiveParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamActiveQuery, int.class, checkTeamActiveParams);
    }

    public int checkTeamInActive(int teamIdx){
        String checkTeamInActiveQuery = "select exists(select * from Team where teamIdx = ? and status = 'inactive')";
        int checkTeamInActiveParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamInActiveQuery, int.class, checkTeamInActiveParams);
    }

    public int checkTeamFinish(int teamIdx){
        String checkTeamFinishQuery = "select exists(select * from Team where teamIdx = ? and status = 'finish')";
        int checkTeamFinishParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamFinishQuery, int.class, checkTeamFinishParams);
    }

    public int checkTeamDeleted(int teamIdx){
        String checkTeamDeletedQuery = "select exists(select * from Team where teamIdx = ? and status != 'deleted')";
        int checkTeamDeletedParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamDeletedQuery, int.class, checkTeamDeletedParams);
    }

    public List<GetReviewRes> getReview(){
        String getReviewQuery = "select discFeatureIdx, name, feature\n" +
                "from DiscFeature\n" +
                "inner join Disc D on DiscFeature.discIdx = D.discIdx\n" +
                "where discFeatureIdx in (5, 10, 7, 4, 13, 18, 19, 20, 33, 30, 35, 32)\n" +
                "order by FIELD(discFeatureIdx, 5, 10, 7, 4, 13, 18, 19, 20, 33, 30, 35, 32)";
        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rsNum) -> new GetReviewRes(
                        rs.getInt("discFeatureIdx"),
                        rs.getString("name"),
                        rs.getString("feature")));
    }

    public List<Member> getMembers(int userIdx, int teamIdx){
        String getMembersQuery = "select TeamMember.userIdx, role, nickName, ageRange, region, occupation, interests\n" +
                "    , case when userImgUrl is null then 0 else userImgUrl end as userImgUrl\n" +
                "    , (select COUNT(*) from Review\n" +
                "        where teamIdx = TeamMember.teamIdx and reviewerIdx = ? and reviewingIdx = TeamMember.userIdx and status = 'active') as isReview\n" +
                "from TeamMember\n" +
                "inner join User U on TeamMember.userIdx = U.userIdx and U.status = 'active'\n" +
                "left join UserDisc UD on U.userIdx = UD.userIdx and isRepDisc = 'Y' and UD.status = 'active'\n" +
                "where teamIdx = ? and TeamMember.status = 'active'\n" +
                "order by TeamMember.createdAt";
        Object[] getMembersParams = new Object[]{userIdx, teamIdx};

        return this.jdbcTemplate.query(getMembersQuery,
                (rs, rsNum) -> new Member(
                        rs.getInt("userIdx"),
                        rs.getString("role"),
                        rs.getString("nickName"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests"),
                        rs.getString("userImgUrl"),
                        rs.getInt("isReview")),
                getMembersParams);
    }

    public int checkUserDisc(int userIdx){
        String checkUserDiscQuery = "select exists(select * from UserDisc\n" +
                "where userIdx = ? and status = 'active' and isRepDisc = 'Y')";
        int checkUserDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserDiscQuery, int.class, checkUserDiscParams);
    }

    public int checkSearchDiscByTeamIdx(int teamIdx){
        String checkSearchDiscQuery = "select exists(select * from Team\n" +
                "inner join SearchDisc SD on Team.userIdx = SD.userIdx where teamIdx = ?)";
        int checkSearchDiscParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkSearchDiscQuery, int.class, checkSearchDiscParams);
    }

    public int getLeaderIdx(int teamIdx){
        String getLeaderIdxQuery = "select userIdx from Team where teamIdx = ?";
        int getLeaderIdxParams = teamIdx;
        return this.jdbcTemplate.queryForObject(getLeaderIdxQuery, int.class, getLeaderIdxParams);
    }

    public UserDiscXy getUserDiscXy(int userIdx){
        String getUserDiscXyQuery = "select x, y from UserDisc\n" +
                "where userIdx = ? and status = 'active' and isRepDisc = 'Y'";
        int getUserDiscXyParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserDiscXyQuery,
                (rs, rsNum) -> new UserDiscXy(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getUserDiscXyParams);
    }

    public SearchDiscAndPercent getSearchDiscAndPercent(int userIdx, UserDiscXy userDiscXy){
        String getSearchDiscQuery = "select x, y, case when x is not null and ? is not null\n" +
                "    then ROUND(100 - SQRT(POWER(x - (?), 2) + POWER(y - (?), 2)) / 33.07752975109958 * 100)\n" +
                "    else 0 end as percent\n" +
                "from SearchDisc\n" +
                "where SearchDisc.userIdx = ? and SearchDisc.status = 'active'";
        Object[] getSearchDiscParams = new Object[]{userDiscXy.getX(), userDiscXy.getX(), userDiscXy.getY(), userIdx};
        return this.jdbcTemplate.queryForObject(getSearchDiscQuery,
                (rs, rsNum) -> new SearchDiscAndPercent(
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getInt("percent")),
                getSearchDiscParams);
    }

    public List<GetTeamRes> getTeam(int teamIdx, int userIdx, SearchDiscAndPercent searchDiscAndPercent, double userDiscX, double userDiscY) {
        String getTeamQuery = "select userIdx as leaderIdx, chatRoomIdx, type, region, interests, title\n" +
                "     , case when length(content) > 40 then CONCAT(LEFT(content, 40), '..')\n" +
                "         else LEFT(content, 40) end as content\n" +
                "     , status\n" +
                "    , (select case when COUNT(*) = 0  then 0 else teamFileUrl end\n" +
                "       from TeamFile where TeamFile.teamIdx = Team.teamIdx and isRepImg = 'Y' and status = 'active') as teamImgUrl\n" +
                "    , (select COUNT(*) from TeamLike where likingIdx = Team.teamIdx and status = 'active') as likeCount\n" +
                "    , (select COUNT(*) from Comment where teamIdx = Team.teamIdx and status = 'active') as commentCount\n" +
                "    , (select COUNT(*) from TeamMember where teamIdx = Team.teamIdx and status = 'active') as memberCount\n" +
                "from Team\n" +
                "where teamIdx = ? and status != 'deleted'";
        int getTeamParams = teamIdx;

        return this.jdbcTemplate.query(getTeamQuery,
                (rs, rsNum) -> new GetTeamRes(
                        rs.getInt("leaderIdx"),
                        rs.getInt("chatRoomIdx"),
                        searchDiscAndPercent,
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("status"),
                        rs.getString("teamImgUrl"),
                        rs.getInt("likeCount"),
                        rs.getInt("commentCount"),
                        rs.getInt("memberCount"),
                        getMembers(userIdx, teamIdx),
                        userDiscX, userDiscY),
                getTeamParams);
    }

    public int deleteTeam(int teamIdx){
        String deleteTeamQuery = "update Team set status = 'deleted' where teamIdx = ?";
        int deleteTeamParams = teamIdx;
        return this.jdbcTemplate.update(deleteTeamQuery, deleteTeamParams);
    }

    public List<Reply> getReplys(int parentIdx){
        String getReplysQuery = "select commentIdx, U.userIdx\n" +
                "     , case when U.status in ('inactive', 'deleted') then '(탈퇴한 회원)' else nickName end as nickName\n" +
                "     , case when U.status in ('inactive', 'deleted') then 0 else userImgUrl end as userImgUrl\n" +
                "     , case when C.status in ('inactive', 'deleted') then '삭제된 댓글입니다.' else content end as content\n" +
                "     , REPLACE(REPLACE(DATE_FORMAT(C.createdAt, '%y.%m.%d %p %h:%i'), 'AM', '오전'), 'PM', '오후') as createdAt\n" +
                "from Comment as C\n" +
                "inner join User U on C.userIdx = U.userIdx\n" +
                "where parentIdx = ?\n" +
                "order by createdAt";
        int getReplysParams = parentIdx;

        return this.jdbcTemplate.query(getReplysQuery,
                (rs, rsNum) -> new Reply(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("content"),
                        rs.getString("createdAt")),
                getReplysParams);
    }

    public List<GetCommentsRes> getComments(int teamIdx){
        String getCommentsQuery = "select commentIdx, U.userIdx\n" +
                "     , case when U.status in ('inactive', 'deleted') then '(탈퇴한 회원)' else nickName end as nickName\n" +
                "     , case when U.status in ('inactive', 'deleted') then 0 else userImgUrl end as userImgUrl\n" +
                "     , case when C.status in ('inactive', 'deleted') then '삭제된 댓글입니다.' else content end as content\n" +
                "     , REPLACE(REPLACE(DATE_FORMAT(C.createdAt, '%y.%m.%d %p %h:%i'), 'AM', '오전'), 'PM', '오후') as createdAt\n" +
                "from Comment as C\n" +
                "inner join User U on C.userIdx = U.userIdx\n" +
                "where teamIdx = ? and parentIdx = 0\n" +
                "order by createdAt";
        int getCommentsParams = teamIdx;

        return this.jdbcTemplate.query(getCommentsQuery,
                (rs, rsNum) -> new GetCommentsRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("content"),
                        rs.getString("createdAt"),
                        getReplys(rs.getInt("commentIdx"))),
                getCommentsParams);
    }

    public int checkCommentIdx(int commentIdx){
        String checkCommentIdxQuery = "select exists(select * from Comment where commentIdx = ? and parentIdx = 0 and status = 'active')";
        int checkCommentIdxParams = commentIdx;
        return this.jdbcTemplate.queryForObject(checkCommentIdxQuery, int.class, checkCommentIdxParams);
    }

    public int createComment(int userIdx, PostCommentReq postCommmentReq){
        String createCommentQuery = "insert into Comment (userIdx, teamIdx, parentIdx, content) VALUES (?, ?, ?, ?)";
        Object[] createCommentParams = new Object[]{userIdx, postCommmentReq.getTeamIdx(), postCommmentReq.getParentIdx(), postCommmentReq.getContent()};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkCommentByUserIdx(int commentIdx, int userIdx){
        String checkCommentByUserIdxQuery = "select exists(select * from Comment where commentIdx = ? and userIdx = ? and status = 'active')";
        Object[] checkCommentByUserIdxParams = new Object[]{commentIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkCommentByUserIdxQuery, int.class, checkCommentByUserIdxParams);
    }

    public int deleteComment(int commentIdx){
        String deleteCommentQuery = "update Comment set status = 'deleted' where commentIdx = ?";
        int deleteCommentParams = commentIdx;
        return this.jdbcTemplate.update(deleteCommentQuery, deleteCommentParams);
    }

    public int updateComment(int commentIdx, PatchCommentReq patchCommentReq){
        String updateCommentQuery = "update Comment set content = ? where commentIdx = ?";
        Object[] updateCommentParams = new Object[]{patchCommentReq.getContent(), commentIdx};
        return this.jdbcTemplate.update(updateCommentQuery, updateCommentParams);
    }

    public int teamDeadline(int teamIdx){
        String teamDeadlineQuery = "update Team set status = 'inactive' where teamIdx = ?";
        int teamDeadlineParams = teamIdx;
        return this.jdbcTemplate.update(teamDeadlineQuery, teamDeadlineParams);
    }

    public int teamFinish(int teamIdx){
        String teamFinishQuery = "update Team set status = 'finish' where teamIdx = ?";
        int teamFinishParams = teamIdx;
        return this.jdbcTemplate.update(teamFinishQuery, teamFinishParams);
    }

    public List<GetTeamImgsRes> getTeamImgs(int teamIdx){
        String getTeamImgsQuery = "select teamfileIdx, isRepImg, fileName, teamFileUrl\n" +
                "from TeamFile\n" +
                "where teamIdx = ? and status = 'active' and type = 'Y'\n" +
                "order by field(isRepImg, 'Y', 'N'), createdAt";
        int getTeamImgsParams = teamIdx;
        return this.jdbcTemplate.query(getTeamImgsQuery,
                (rs, rsNum) -> new GetTeamImgsRes(
                        rs.getInt("teamfileIdx"),
                        rs.getString("isRepImg"),
                        rs.getString("fileName"),
                        rs.getString("teamFileUrl")),
                        getTeamImgsParams);
    }

    public PastUserDisc getPastUserDisc(int userIdx){
        String getPastUserDiscQuery = "select userDiscIdx, x, y, dPercent, ipercent, sPercent, cPercent from UserDisc\n" +
                "where userIdx = ? and isRepDisc = 'Y' and status = 'active'";
        int getPastUserDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(getPastUserDiscQuery,
                (rs, rsNum) -> new PastUserDisc(
                        rs.getInt("userDiscIdx"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getDouble("dPercent"),
                        rs.getDouble("iPercent"),
                        rs.getDouble("sPercent"),
                        rs.getDouble("cPercent")),
                getPastUserDiscParams);
    }

    public int updateUserDisc(int userDiscIdx, double x, double y, double dPercent, double iPercent, double sPercent, double cPercent){
        String updateUserDiscQuery = "update UserDisc set x = ?, y = ?, dPercent = ?, ipercent = ?, sPercent = ?, cPercent = ? where userDiscIdx = ?";
        Object[] updateUserDiscParams = new Object[]{x, y, dPercent, iPercent, sPercent, cPercent, userDiscIdx};
        return this.jdbcTemplate.update(updateUserDiscQuery, updateUserDiscParams);
    }

    public int checkPastReview(int teamIdx, int reviewerIdx, int reviewingIdx){
        String checkPastReviewQuery = "select exists(select * from Review where teamIdx = ? and reviewerIdx = ? and reviewingIdx = ? and status = 'active')";
        Object[] checkPastReviewParams = new Object[]{teamIdx, reviewerIdx, reviewingIdx};
        return this.jdbcTemplate.queryForObject(checkPastReviewQuery, int.class, checkPastReviewParams);
    }

    public int createReview(int reviewerIdx, int reviewingIdx, PostReviewReq postReviewReq, double[] reviewDisc) {
        String createReviewQuery = "insert into Review (reviewerIdx, reviewingIdx, teamIdx, name1, name2, name3, x, y) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createReviewParams = new Object[]{reviewerIdx, reviewingIdx, postReviewReq.getTeamIdx(), postReviewReq.getReviews().get(0).getName(), postReviewReq.getReviews().get(1).getName()
                , postReviewReq.getReviews().get(2).getName(), reviewDisc[0], reviewDisc[1]};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<String> getTeamFilter(int userIdx, int category) {
        String getTeamFilterQuery = "select name from TeamFilter where userIdx = ? and category = ? and status = 'active'";
        Object[] getTeamFilterParams = new Object[]{userIdx, category};

        return this.jdbcTemplate.query(getTeamFilterQuery,
                (rs, rsNum) -> (rs.getString("name")),
                getTeamFilterParams);
    }

    public List<GetTeamMatchRes> getTeamMatches(UserDiscXy userDiscXy, int userIdx, List<String> type, List<String> region, List<String> interests) {
        String getTeamMatchesQuery = "select teamIdx, userIdx as leaderIdx\n" +
                "      , (select x from SearchDisc\n" +
                "        where SearchDisc.userIdx = Team.userIdx and SearchDisc.status = 'active') as x\n" +
                "     , (select y from SearchDisc\n" +
                "        where SearchDisc.userIdx = Team.userIdx and SearchDisc.status = 'active') as y\n" +
                "     , (select case when x is not null then x else 0 end\n" +
                "        from SearchDisc\n" +
                "        where SearchDisc.userIdx = Team.userIdx and SearchDisc.status = 'active') as x\n" +
                "     , (select case when x is not null and ? is not null\n" +
                "        then ROUND(100 - SQRT(POWER(x - (?), 2) + POWER(y - (?), 2)) / 33.07752975109958 * 100)\n" +
                "        else 0 end as percent\n" +
                "        from SearchDisc\n" +
                "        where SearchDisc.userIdx = Team.userIdx and SearchDisc.status = 'active') as percent\n" +
                "     , type, region, interests, title\n" +
                "     , case when length(content) > 40 then CONCAT(LEFT(content, 40), '..')\n" +
                "         else LEFT(content, 40) end as content\n" +
                "    , (select case when COUNT(*) = 0  then 0 else teamFileUrl end\n" +
                "       from TeamFile where TeamFile.teamIdx = Team.teamIdx and isRepImg = 'Y' and status = 'active') as teamImgUrl\n" +
                "    , (select COUNT(*) from TeamLike where likingIdx = Team.teamIdx and status = 'active') as likeCount\n" +
                "    , (select COUNT(*) from Comment where teamIdx = Team.teamIdx and status = 'active') as commentCount\n" +
                "    , (select COUNT(*) from TeamMember where teamIdx = Team.teamIdx and status = 'active') as memberCount\n" +
                "from Team\n" +
                "where status = 'active' and userIdx != ?\n" +
                "    and teamIdx not in (select likingIdx from TeamLike where likerIdx = ? and TeamLike.status = 'active')\n" +
                "    and teamIdx not in (select passingIdx from TeamPass where passerIdx = ? and TeamPass.status = 'active')\n" +
                "    and userIdx not in (select blockedUserIdx from Block where blockUserIdx = ? and Block.status = 'active')\n" +
                "    and userIdx not in (select reportedUserIdx from Report where Report.userIdx = ?)\n" +
                "    and type in (?, ?, ?, ?, ?, ?)\n" +
                "        and type not in (' ')\n" +
                "    and region in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "        and region not in (' ')\n" +
                "    and interests in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "        and interests not in (' ')\n" +
                "order by percent desc, teamIdx desc";

        Object[] getTeamMatchesParams = new Object[]{userDiscXy.getX(), userDiscXy.getX(), userDiscXy.getY(), userIdx, userIdx, userIdx, userIdx, userIdx
                , type.get(0), type.get(1), type.get(2), type.get(3), type.get(4), type.get(5)
                , region.get(0), region.get(1), region.get(2), region.get(3), region.get(4), region.get(5), region.get(6), region.get(7)
                , region.get(8), region.get(9), region.get(10), region.get(11), region.get(12), region.get(13), region.get(14), region.get(15), region.get(16)
                , interests.get(0), interests.get(1), interests.get(2), interests.get(3), interests.get(4), interests.get(5)
                , interests.get(6), interests.get(7), interests.get(8), interests.get(9), interests.get(10), interests.get(11), interests.get(12), interests.get(13)};

        return this.jdbcTemplate.query(getTeamMatchesQuery,
                (rs, rsNum) -> new GetTeamMatchRes(
                        rs.getInt("teamIdx"),
                        rs.getInt("leaderIdx"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getInt("percent"),
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("teamImgUrl"),
                        rs.getInt("likeCount"),
                        rs.getInt("commentCount"),
                        rs.getInt("memberCount"),
                        getMembers(userIdx, rs.getInt("teamIdx"))),
                getTeamMatchesParams);
    }

}
