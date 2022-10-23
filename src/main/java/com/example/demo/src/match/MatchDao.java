package com.example.demo.src.match;

import com.example.demo.src.match.model.GetMatchedUserRes;
import com.example.demo.src.match.model.GetUserFilterRes;
import com.example.demo.src.match.model.GetUserLikerRes;
import com.example.demo.src.match.model.GetUserLikingRes;
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

    public int checkUserIdx(int userIdx){
        String checkUserIdxQuery = "select exists(select userIdx from User where userIdx = ? and status = 'active');";
        int checkUserIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserIdxQuery,
                int.class,
                checkUserIdxParams);
    }

    public int checkPreUserLike(int likerIdx, int likingIdx){
        String checkPreUserLikeQuery = "select exists(select likerIdx from UserLike where likerIdx = ? and likingIdx = ? and status = 'active')";
        Object[] checkPreUserLikeParams = new Object[]{likerIdx, likingIdx};
        return this.jdbcTemplate.queryForObject(checkPreUserLikeQuery,
                int.class,
                checkPreUserLikeParams);
    }

    public int checkUserLikeHist(int likerIdx, int likingIdx){
        String checkUserLikeHistQuery = "select case\n" +
                "        when COUNT(*) = 0 then 0\n" +
                "        else userLikeIdx end as userLikeIdx\n" +
                "from UserLike\n" +
                "where likerIdx = ? and likingIdx = ? and status in ('inactive', 'deleted')";
        Object[] checkUserLikeHistParams = new Object[]{likerIdx, likingIdx};
        return this.jdbcTemplate.queryForObject(checkUserLikeHistQuery,
                int.class,
                checkUserLikeHistParams);
    }

    public int updateUserLike(int userLikeIdx){
        String updateUserLikeQuery = "update UserLike set status = 'active' where userLikeIdx = ?";
        int updateUserLikeParams = userLikeIdx;
        return this.jdbcTemplate.update(updateUserLikeQuery, updateUserLikeParams);
    }

    public int createUserLike(int likerIdx, int likingIdx) {
        String createUserLikeQuery = "insert into UserLike (likerIdx, likingIdx) VALUES (?, ?)";
        Object[] createUserLikeParams = new Object[]{likerIdx, likingIdx};
        this.jdbcTemplate.update(createUserLikeQuery, createUserLikeParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteUserLike(int likerIdx, int likingIdx){
        String deleteUserLikeQuery = "update UserLike set status = 'deleted' where likerIdx = ? and likingIdx = ?";
        Object[] deleteUserLikeParams = new Object[]{likerIdx, likingIdx};

        return this.jdbcTemplate.update(deleteUserLikeQuery, deleteUserLikeParams);
    }

    public int checkPreUserPass(int passerIdx, int passingIdx){
        String checkPreUserPassQuery = "select exists(select userPassIdx from UserPass where passerIdx = ? and passingIdx = ? and status = 'active')";
        Object[] checkPreUserPassParams = new Object[]{passerIdx, passingIdx};
        return this.jdbcTemplate.queryForObject(checkPreUserPassQuery,
                int.class,
                checkPreUserPassParams);
    }

    public int checkUserPassHist(int passerIdx, int passingIdx){
        String checkUserPassHistQuery = "select case\n" +
                "        when COUNT(*) = 0 then 0\n" +
                "        else userPassIdx end as userPassIdx\n" +
                "from UserPass\n" +
                "where passerIdx = ? and passingIdx = ? and status in ('inactive', 'deleted')";
        Object[] checkUserPassHistParams = new Object[]{passerIdx, passingIdx};
        return this.jdbcTemplate.queryForObject(checkUserPassHistQuery,
                int.class,
                checkUserPassHistParams);
    }

    public int updateUserPass(int userPassIdx){
        String updateUserPassQuery = "update UserPass set status = 'active' where userPassIdx = ?";
        int updateUserPassParams = userPassIdx;
        return this.jdbcTemplate.update(updateUserPassQuery, updateUserPassParams);
    }

    public int createUserPass(int passerIdx, int passingIdx) {
        String createUserPassQuery = "insert into UserPass (passerIdx, passingIdx) VALUES (?, ?)";
        Object[] createUserPassParams = new Object[]{passerIdx, passingIdx};
        this.jdbcTemplate.update(createUserPassQuery, createUserPassParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetUserLikingRes> getUserLikings(int userIdx){
        String getUserLikingsQuery = "select userIdx as likingIdx, nickName, userImgUrl\n" +
                ", gender, ageRange, region, occupation, interests\n" +
                "from User\n" +
                "inner join UserLike UL on User.userIdx = UL.likingIdx\n" +
                "where likerIdx = ? and UL.status = 'active'\n" +
                "order by UL.updatedAt DESC";
        int getUserLikingsParams = userIdx;

        return this.jdbcTemplate.query(getUserLikingsQuery,
                (rs, rsNum) -> new GetUserLikingRes(
                        rs.getInt("likingIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests")),
                getUserLikingsParams);
    }

    public List<GetUserLikerRes> getUserLikers(int userIdx){
        String getUserLikersQuery = "select userIdx as likerIdx, nickName, userImgUrl\n" +
                ", gender, ageRange, region, occupation, interests\n" +
                "from User\n" +
                "inner join UserLike UL on User.userIdx = UL.likerIdx\n" +
                "where likingIdx = ? and UL.status = 'active'\n" +
                "order by UL.updatedAt DESC";
        int getUserLikersParams = userIdx;

        return this.jdbcTemplate.query(getUserLikersQuery,
                (rs, rsNum) -> new GetUserLikerRes(
                        rs.getInt("likerIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests")),
                getUserLikersParams);
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

    public void createProjectTypeFilter(int userIdx, String name){
        String createProjectTypeFilterQuery = "insert into ProjectFilter (userIdx, category, name) values (?, 1, ?)";
        Object[] createProjectTypeFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createProjectTypeFilterQuery, createProjectTypeFilterParams);
    }

    public void createProjectRegionFilter(int userIdx, String name){
        String createProjectRegionFilterQuery = "insert into ProjectFilter (userIdx, category, name) values (?, 2, ?)";
        Object[] createProjectRegionFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createProjectRegionFilterQuery, createProjectRegionFilterParams);
    }

    public void createProjectInterestsFilter(int userIdx, String name){
        String createProjectInterestsFilterQuery = "insert into ProjectFilter (userIdx, category, name) values (?, 3, ?)";
        Object[] createProjectInterestsFilterParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createProjectInterestsFilterQuery, createProjectInterestsFilterParams);
    }

}
