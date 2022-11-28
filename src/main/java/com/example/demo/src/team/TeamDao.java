package com.example.demo.src.team;

import com.example.demo.src.team.model.*;
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

    public int createTeam(int userIdx, PostTeamReq postTeamReq) {
        String createTeamQuery = "insert into Team (userIdx, title, content, type, region, interests) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] createTeamParams = new Object[]{userIdx, postTeamReq.getTitle(), postTeamReq.getContent(), postTeamReq.getType(), postTeamReq.getRegion(), postTeamReq.getInterests()};
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

    public int checkTeamDeleted(int teamIdx){
        String checkTeamDeletedQuery = "select exists(select * from Team where teamIdx = ? and status != 'deleted')";
        int checkTeamDeletedParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamDeletedQuery, int.class, checkTeamDeletedParams);
    }

    public List<Member> getMembers(int teamIdx){
        String getMembersQuery = "select TeamMember.userIdx, role, nickName, ageRange, region, occupation, interests\n" +
                "    , case when userImgUrl is null then 0 else userImgUrl end as userImgUrl\n" +
                "    , case when x is null then 0 else x end as x\n" +
                "     , case when y is null then 0 else y end as y\n" +
                "from TeamMember\n" +
                "inner join User U on TeamMember.userIdx = U.userIdx and U.status = 'active'\n" +
                "left join UserDisc UD on U.userIdx = UD.userIdx and isRepDisc = 'Y' and UD.status = 'active'\n" +
                "where teamIdx = ? and TeamMember.status = 'active'\n" +
                "order by TeamMember.updatedAt";
        int getMembersParams = teamIdx;

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
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getMembersParams);
    }

    public List<GetTeamRes> getTeam(int teamIdx) {
        String getTeamQuery = "select userIdx as leaderIdx, type, region, interests, title\n" +
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
                        getMembers(teamIdx)),
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


}
