package com.example.demo.src.team;

import com.example.demo.src.report.model.PostReportReq;
import com.example.demo.src.team.model.PostTeamReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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

    public int createMember(int teamIdx, int userIdx, String role) {
        String createMemberQuery = "insert into TeamMember (teamIdx, userIdx, role) VALUES (?, ?, ?)";
        Object[] createMemberParams = new Object[]{teamIdx, userIdx, role};
        this.jdbcTemplate.update(createMemberQuery, createMemberParams);

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

}
