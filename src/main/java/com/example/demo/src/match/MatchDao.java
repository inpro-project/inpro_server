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

    public int checkUserFilterByName(int userIdx, String name){
        String checkUserFilterByNameQuery = "select case when count(*) = 0 then 0 else userFilterIdx end as userFilterIdx\n" +
                "    from UserFilter where userIdx = ? and name like concat('%', ?,'%')";
        Object[] checkUserFilterByNameParams = new Object[]{userIdx, name};
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

    public int checkTeamFilterByName(int userIdx, String name){
        String checkTeamFilterByNameQuery = "select case when count(*) = 0 then 0 else teamFilterIdx end as teamFilterIdx\n" +
                "    from TeamFilter where userIdx = ? and name like concat('%', ?,'%')";
        Object[] checkTeamFilterByNameParams = new Object[]{userIdx, name};
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

    public List<String> getUserFilter(int userIdx, int category) {
        String getUserFilterQuery = "select name from UserFilter where userIdx = ? and category = ? and status = 'active'";
        Object[] getUserFilterParams = new Object[]{userIdx, category};

        return this.jdbcTemplate.query(getUserFilterQuery,
                (rs, rsNum) -> (rs.getString("name")),
                getUserFilterParams);
    }

    public int checkSearchDisc(int userIdx){
        String checkSearchDiscQuery = "select exists(select * from SearchDisc where userIdx = ?)";
        int checkSearchDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkSearchDiscQuery,
                int.class,
                checkSearchDiscParams);
    }

    public SearchDiscXy getPastSearchDisc(int userIdx) {
        String getPastSearchDiscQuery = "select x, y from SearchDisc where userIdx = ?";
        int getPastSearchDiscParams = userIdx;
        return jdbcTemplate.queryForObject(getPastSearchDiscQuery,
                (rs, rsNum) -> new SearchDiscXy(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getPastSearchDiscParams);
    }

    public SearchDiscXy getNewSearchDisc(int userIdx) {
        String getNewSearchDiscQuery = "select case when COUNT(*) > 5 then AVG(x) else null end as x\n" +
                "    , case when COUNT(*) > 5 then AVG(y) else null end as y\n" +
                "from UserLike\n" +
                "inner join User U on UserLike.likingIdx = U.userIdx and U.status = 'active'\n" +
                "inner join UserDisc UD on U.userIdx = UD.userIdx and isRepDisc = 'Y' and UD.status = 'active'\n" +
                "where likerIdx = ? and UserLike.status = 'active'";
        int getNewSearchDiscParams = userIdx;
        return jdbcTemplate.queryForObject(getNewSearchDiscQuery,
                (rs, rsNum) -> new SearchDiscXy(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getNewSearchDiscParams);
    }

    public void createSearchDisc(int userIdx, double x, double y){
        String createSearchDiscQuery = "insert into SearchDisc (userIdx, x, y) values (?, ?, ?)";
        Object[] createSearchDiscParams = new Object[]{userIdx, x, y};
        this.jdbcTemplate.update(createSearchDiscQuery, createSearchDiscParams);
    }

    public int updateSearchDisc(int userIdx, double x, double y){
        String updateSearchDiscQuery = "update SearchDisc set x = ?, y = ? where userIdx = ?";
        Object[] updateSearchDiscParams = new Object[]{x, y, userIdx};
        return this.jdbcTemplate.update(updateSearchDiscQuery, updateSearchDiscParams);
    }

    public List<GetUserMatchRes> getUserMatches(int userIdx, SearchDiscXy searchDiscXy, List<String> ageRange, List<String> region, List<String> occupation, List<String> interests) {
        String getUserMatchesQuery = "select User.userIdx, nickName, userImgUrl\n" +
                "     , gender, ageRange, comment, region, occupation, interests\n" +
                "     , case when x is not null then x else 0 end as x\n" +
                "     , case when y is not null then y else 0 end as y\n" +
                "     , case when x is not null and ? is not null\n" +
                "         then ROUND(100 - SQRT(POWER(x - (?), 2) + POWER(y - (?), 2)) / 33.07752975109958 * 100)\n" +
                "        else 0 end as percent\n" +
                "from User\n" +
                "left join UserDisc UD on User.userIdx = UD.userIdx and UD.status = 'active' and isRepDisc = 'Y'\n" +
                "where User.userIdx not in(?)\n" +
                "  and User.userIdx not in (select passingIdx from UserPass where passerIdx = ? and UserPass.status = 'active')\n" +
                "  and User.userIdx not in (select blockedUserIdx from Block where blockUserIdx = ? and Block.status = 'active')\n" +
                "  and User.userIdx not in (select reportedUserIdx from Report where Report.userIdx = ?)\n" +
                "  and User.userIdx not in (select likingIdx from UserLike where likerIdx = ? and UserLike.status = 'active')\n" +
                "and User.status = 'active'\n" +
                "and ageRange in (?, ?, ?, ?, ?, ?)\n" +
                "  and ageRange not in (' ')\n" +
                "and region in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "  and region not in (' ')\n" +
                "and occupation in (?, ?, ?, ?, ?)\n" +
                "  and occupation not in (' ')\n" +
                "and interests in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "  and interests not in (' ')\n" +
                "order by percent DESC, userIdx DESC";

        Object[] getUserMatchesParams = new Object[]{searchDiscXy.getX(), searchDiscXy.getX(), searchDiscXy.getY(), userIdx, userIdx, userIdx, userIdx, userIdx
                , ageRange.get(0), ageRange.get(1), ageRange.get(2), ageRange.get(3), ageRange.get(4), ageRange.get(5)
                , region.get(0), region.get(1), region.get(2), region.get(3), region.get(4), region.get(5), region.get(6), region.get(7)
                , region.get(8), region.get(9), region.get(10), region.get(11), region.get(12), region.get(13), region.get(14), region.get(15), region.get(16)
                , occupation.get(0), occupation.get(1), occupation.get(2), occupation.get(3), occupation.get(4)
                , interests.get(0), interests.get(1), interests.get(2), interests.get(3), interests.get(4), interests.get(5)
                , interests.get(6), interests.get(7), interests.get(8), interests.get(9), interests.get(10), interests.get(11), interests.get(12), interests.get(13)};

        return this.jdbcTemplate.query(getUserMatchesQuery,
                (rs, rsNum) -> new GetUserMatchRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("comment"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getInt("percent")),
                getUserMatchesParams);
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
