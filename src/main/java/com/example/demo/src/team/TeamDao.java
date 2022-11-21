package com.example.demo.src.team;

import com.example.demo.src.team.model.GetTeamsRes;
import com.example.demo.src.team.model.PostMemberReq;
import com.example.demo.src.team.model.PostTeamReq;
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

    public int checkTeamIdx(int teamIdx, int leaderIdx){
        String checkTeamIdxQuery = "select exists(select * from TeamMember where teamIdx = ? and userIdx = ? and role = '리더')";
        Object[] checkTeamIdxParams = new Object[]{teamIdx, leaderIdx};
        return this.jdbcTemplate.queryForObject(checkTeamIdxQuery, int.class, checkTeamIdxParams);
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

}
