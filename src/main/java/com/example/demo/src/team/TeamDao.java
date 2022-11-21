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
        String checkUserIdxQuery = "select exists(select * from User where userIdx = ?)";
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkUserIdxParams);
    }

    public int checkTeamIdxByLeader(int teamIdx, int leaderIdx){
        String checkTeamIdxByLeaderQuery = "select exists(select * from TeamMember where teamIdx = ? and userIdx = ? and role = '리더')";
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
        String getTeamsQuery = "select teamIdx, title, type, region, interests, status from Team " +
                "where userIdx = ? and status in('active', 'inactive') order by createdAt DESC";
        int getTeamsParams = userIdx;
        return this.jdbcTemplate.query(getTeamsQuery,
                (rs, rsNum) -> new GetTeamsRes(
                        rs.getInt("teamIdx"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests"),
                        rs.getString("status")),
                getTeamsParams);
    }

    public int checkTeamIdx(int teamIdx){
        String checkTeamIdxQuery = "select exists(select * from Team where teamIdx = ? and status in ('active', 'inactive'))";
        int checkTeamIdxParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamIdxQuery, int.class, checkTeamIdxParams);
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
                "where teamIdx = ? and status in ('active', 'inactive')";
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

}
