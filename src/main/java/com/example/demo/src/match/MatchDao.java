package com.example.demo.src.match;

import com.example.demo.src.match.model.*;
import com.example.demo.src.user.model.PatchPortfolioReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MatchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMatchedUserRes> getMatchedUsers(int userIdx){
        String getMatchedUsersQuery = "select userIdx as matchedUserIdx, nickName, userImgUrl\n" +
                ", gender, ageRange, region, occupation, interests\n" +
                "from (select userIdx, nickName, userImgUrl, gender, ageRange, region, occupation, interests\n" +
                "from User\n" +
                "inner join UserLike UL on User.userIdx = UL.likerIdx and UL.status = 'active'\n" +
                "where likingIdx = ?\n" +
                "order by UL.updatedAt DESC) as User\n" +
                "inner join UserLike UL on User.userIdx = UL.likingIdx and status = 'active'\n" +
                "where likerIdx = ?\n" +
                "order by UL.updatedAt DESC";
        Object[] getMatchedUsersParams = new Object[]{userIdx, userIdx};

        return this.jdbcTemplate.query(getMatchedUsersQuery,
                (rs, rsNum) -> new GetMatchedUserRes(
                        rs.getInt("matchedUserIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests")),
                getMatchedUsersParams);
    }

    public void createAgeRangeFilter(int userIdx, String name){
        String createAgeRangeFilterQuery = "insert into UserFilter (userIdx, category, name) values (?, 1, ?)";
        Object[] createAgeRangeFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createAgeRangeFilterQuery, createAgeRangeFilterParams);
    }

    public void createRegionFilter(int userIdx, String name){
        String createRegionFilterQuery = "insert into UserFilter (userIdx, category, name) values (?, 2, ?)";
        Object[] createRegionFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createRegionFilterQuery, createRegionFilterParams);
    }

    public void createOccupationFilter(int userIdx, String name){
        String createOccupationFilterQuery = "insert into UserFilter (userIdx, category, name) values (?, 3, ?)";
        Object[] createOccupationFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createOccupationFilterQuery, createOccupationFilterParams);
    }

    public void createInterestsFilter(int userIdx, String name){
        String createInterestsFilterQuery = "insert into UserFilter (userIdx, category, name) values (?, 4, ?)";
        Object[] createInterestsFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createInterestsFilterQuery, createInterestsFilterParams);
    }

    public List<GetUserFilterRes> getUserFilters(int userIdx){
        String getUserFiltersQuery = "select userFilterIdx, category, name\n" +
                "from UserFilter\n" +
                "where userIdx = ? and status = 'active'\n" +
                "order by category";
        int getUserFiltersParams = userIdx;
        return this.jdbcTemplate.query(getUserFiltersQuery,
                (rs, rsNum) -> new GetUserFilterRes(
                        rs.getInt("userFilterIdx"),
                        rs.getInt("category"),
                        rs.getString("name")),
                getUserFiltersParams);
    }

    public int checkUserFilterByName(int userIdx, int category, String name){
        String checkUserFilterByNameQuery = "select case when count(*) = 0 then 0 else userFilterIdx end as userFilterIdx\n" +
                "    from UserFilter where userIdx = ? and category = ? and name like concat('%', ?,'%')";
        Object[] checkUserFilterByNameParams = new Object[]{userIdx, category, name};
        return this.jdbcTemplate.queryForObject(checkUserFilterByNameQuery,
                int.class,
                checkUserFilterByNameParams);
    }

    public int checkUserFilterByIdx(int userIdx, int userFilterIdx){
        String checkUserFilterByIdxQuery = "select exists(select userFilterIdx from UserFilter where userIdx = ? and userFilterIdx = ? and status = 'active')";
        Object[] checkUserFilterByIdxParams = new Object[]{userIdx, userFilterIdx};
        return this.jdbcTemplate.queryForObject(checkUserFilterByIdxQuery,
                int.class,
                checkUserFilterByIdxParams);
    }

    public void updateUserFilter(int userIdx, int userFilterIdx){
        String updateUserFilterQuery = "update UserFilter set status = 'active' where userIdx = ? and userFilterIdx = ?";
        Object[] updateUserFilterParams = new Object[]{userIdx, userFilterIdx};
        this.jdbcTemplate.update(updateUserFilterQuery, updateUserFilterParams);
    }

    public void deleteUserFilter(int userIdx, Integer userFilterIdx){
        String deleteUserFilterQuery = "update UserFilter set status = 'deleted' where userIdx = ? and userFilterIdx = ?";
        Object[] deleteUserFilterParams = new Object[]{userIdx, userFilterIdx};
        this.jdbcTemplate.update(deleteUserFilterQuery, deleteUserFilterParams);
    }

    public void createTeamTypeFilter(int userIdx, String name){
        String createTeamTypeFilterQuery = "insert into TeamFilter (userIdx, category, name) values (?, 1, ?)";
        Object[] createTeamTypeFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createTeamTypeFilterQuery, createTeamTypeFilterParams);
    }

    public void createTeamRegionFilter(int userIdx, String name){
        String createTeamRegionFilterQuery = "insert into TeamFilter (userIdx, category, name) values (?, 2, ?)";
        Object[] createTeamRegionFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createTeamRegionFilterQuery, createTeamRegionFilterParams);
    }

    public void createTeamInterestsFilter(int userIdx, String name){
        String createTeamInterestsFilterQuery = "insert into TeamFilter (userIdx, category, name) values (?, 3, ?)";
        Object[] createTeamInterestsFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createTeamInterestsFilterQuery, createTeamInterestsFilterParams);
    }

    public List<GetTeamFilterRes> getTeamFilters(int userIdx){
        String getTeamFiltersQuery = "select teamFilterIdx, category, name\n" +
                "from TeamFilter\n" +
                "where userIdx = ? and status = 'active'\n" +
                "order by category";
        int getTeamFiltersParams = userIdx;
        return this.jdbcTemplate.query(getTeamFiltersQuery,
                (rs, rsNum) -> new GetTeamFilterRes(
                        rs.getInt("teamFilterIdx"),
                        rs.getInt("category"),
                        rs.getString("name")),
                getTeamFiltersParams);
    }

    public int checkTeamFilterByName(int userIdx, int category, String name){
        String checkTeamFilterByNameQuery = "select case when count(*) = 0 then 0 else teamFilterIdx end as teamFilterIdx\n" +
                "    from TeamFilter where userIdx = ? and category = ? and name like concat('%', ?,'%')";
        Object[] checkTeamFilterByNameParams = new Object[]{userIdx, category, name};
        return this.jdbcTemplate.queryForObject(checkTeamFilterByNameQuery,
                int.class,
                checkTeamFilterByNameParams);
    }

    public int checkTeamFilterByIdx(int userIdx, int teamFilterIdx){
        String checkTeamFilterByIdxQuery = "select exists(select teamFilterIdx from TeamFilter where userIdx = ? and teamFilterIdx = ? and status = 'active')";
        Object[] checkTeamFilterByIdxParams = new Object[]{userIdx, teamFilterIdx};
        return this.jdbcTemplate.queryForObject(checkTeamFilterByIdxQuery,
                int.class,
                checkTeamFilterByIdxParams);
    }

    public void updateTeamFilter(int userIdx, int teamFilterIdx){
        String updateTeamFilterQuery = "update TeamFilter set status = 'active' where userIdx = ? and teamFilterIdx = ?";
        Object[] updateTeamFilterParams = new Object[]{userIdx, teamFilterIdx};
        this.jdbcTemplate.update(updateTeamFilterQuery, updateTeamFilterParams);
    }

    public void deleteTeamFilter(int userIdx, Integer teamFilterIdx){
        String deleteTeamFilterQuery = "update TeamFilter set status = 'deleted' where userIdx = ? and teamFilterIdx = ?";
        Object[] deleteTeamFilterParams = new Object[]{userIdx, teamFilterIdx};
        this.jdbcTemplate.update(deleteTeamFilterQuery, deleteTeamFilterParams);
    }

    public List<GetMatchedTeamRes> getMatchedTeams(int userIdx){
        String getTeamMatchesQuery = "select T.teamIdx as matchedTeamIdx\n" +
                "     , (select case when count(*) = 0 then 0 else teamFileUrl end as teamFileUrl\n" +
                "        from TeamFile\n" +
                "        where TeamFile.teamIdx = T.teamIdx and T.status = 'active' and isRepImg = 'Y') as teamRepUrl\n" +
                "     , title, type, region, interests, T.status\n" +
                "from TeamMember\n" +
                "inner join Team T on TeamMember.teamIdx = T.teamIdx\n" +
                "where TeamMember.userIdx = ? and role != '리더' and T.status != 'deleted'\n" +
                "order by TeamMember.updatedAt DESC";
        int getTeamMatchesParams = userIdx;

        return jdbcTemplate.query(getTeamMatchesQuery,
                (rs, rsNum) -> new GetMatchedTeamRes(
                        rs.getInt("matchedTeamIdx"),
                        rs.getString("teamRepUrl"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests"),
                        rs.getString("status")),
                getTeamMatchesParams);
    }

}
