package com.example.demo.src.like;

import com.example.demo.src.like.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LikeDao {

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

    public int deleteUserPass(int passerIdx, int passingIdx){
        String deleteUserPassQuery = "update UserPass set status = 'deleted' where passerIdx = ? and passingIdx = ?";
        Object[] deleteUserPassParams = new Object[]{passerIdx, passingIdx};

        return this.jdbcTemplate.update(deleteUserPassQuery, deleteUserPassParams);
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

    public int checkTeamIdx(int teamIdx){
        String checkTeamIdxQuery = "select exists(select teamIdx from Team where teamIdx = ? and status = 'active')";
        int checkTeamIdxParams = teamIdx;
        return this.jdbcTemplate.queryForObject(checkTeamIdxQuery,
                int.class,
                checkTeamIdxParams);
    }

    public int checkPreTeamLike(int likerIdx, int likingIdx){
        String checkPreTeamLikeQuery = "select exists(select likerIdx from TeamLike where likerIdx = ? and likingIdx = ? and status = 'active')";
        Object[] checkPreTeamLikeParams = new Object[]{likerIdx, likingIdx};
        return this.jdbcTemplate.queryForObject(checkPreTeamLikeQuery,
                int.class,
                checkPreTeamLikeParams);
    }

    public int checkTeamLikeHist(int likerIdx, int likingIdx){
        String checkTeamLikeHistQuery = "select case\n" +
                "        when COUNT(*) = 0 then 0\n" +
                "        else teamLikeIdx end as teamLikeIdx\n" +
                "from TeamLike\n" +
                "where likerIdx = ? and likingIdx = ? and status in ('inactive', 'deleted')";
        Object[] checkTeamLikeHistParams = new Object[]{likerIdx, likingIdx};
        return this.jdbcTemplate.queryForObject(checkTeamLikeHistQuery,
                int.class,
                checkTeamLikeHistParams);
    }

    public int updateTeamLike(int teamLikeIdx){
        String updateTeamLikeQuery = "update TeamLike set status = 'active' where teamLikeIdx = ?";
        int updateTeamLikeParams = teamLikeIdx;
        return this.jdbcTemplate.update(updateTeamLikeQuery, updateTeamLikeParams);
    }

    public int createTeamLike(int likerIdx, int likingIdx) {
        String createTeamLikeQuery = "insert into TeamLike (likerIdx, likingIdx) VALUES (?, ?)";
        Object[] createTeamLikeParams = new Object[]{likerIdx, likingIdx};
        this.jdbcTemplate.update(createTeamLikeQuery, createTeamLikeParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteTeamLike(int likerIdx, int likingIdx){
        String deleteTeamLikeQuery = "update TeamLike set status = 'deleted' where likerIdx = ? and likingIdx = ?";
        Object[] deleteTeamLikeParams = new Object[]{likerIdx, likingIdx};
        return this.jdbcTemplate.update(deleteTeamLikeQuery, deleteTeamLikeParams);
    }

    public int checkPreTeamPass(int passerIdx, int passingIdx){
        String checkPreTeamPassQuery = "select exists(select teamPassIdx from TeamPass where passerIdx = ? and passingIdx = ? and status = 'active')";
        Object[] checkPreTeamPassParams = new Object[]{passerIdx, passingIdx};
        return this.jdbcTemplate.queryForObject(checkPreTeamPassQuery,
                int.class,
                checkPreTeamPassParams);
    }

    public int checkTeamPassHist(int passerIdx, int passingIdx){
        String checkTeamPassHistQuery = "select case\n" +
                "        when COUNT(*) = 0 then 0\n" +
                "        else teamPassIdx end as teamPassIdx\n" +
                "from TeamPass\n" +
                "where passerIdx = ? and passingIdx = ? and status in ('inactive', 'deleted')";
        Object[] checkTeamPassHistParams = new Object[]{passerIdx, passingIdx};
        return this.jdbcTemplate.queryForObject(checkTeamPassHistQuery,
                int.class,
                checkTeamPassHistParams);
    }

    public int updateTeamPass(int teamPassIdx){
        String updateTeamPassQuery = "update TeamPass set status = 'active' where teamPassIdx = ?";
        int updateTeamPassParams = teamPassIdx;
        return this.jdbcTemplate.update(updateTeamPassQuery, updateTeamPassParams);
    }

    public int createTeamPass(int passerIdx, int passingIdx) {
        String createTeamPassQuery = "insert into TeamPass (passerIdx, passingIdx) VALUES (?, ?)";
        Object[] createTeamPassParams = new Object[]{passerIdx, passingIdx};
        this.jdbcTemplate.update(createTeamPassQuery, createTeamPassParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteTeamPass(int passerIdx, int passingIdx){
        String deleteTeamPassQuery = "update TeamPass set status = 'deleted' where passerIdx = ? and passingIdx = ?";
        Object[] deleteTeamPassParams = new Object[]{passerIdx, passingIdx};

        return this.jdbcTemplate.update(deleteTeamPassQuery, deleteTeamPassParams);
    }

    public TeamRepImg getTeamRepImg(int teamIdx){
        String getTeamRepImgQuery = "select case when count(*) = 0 then 0 else fileName end as fileName\n" +
                "     , case when count(*) = 0 then 0 else teamFileUrl end as teamFileUrl\n" +
                "from TeamFile\n" +
                "where teamIdx = ? and status = 'active' and type = 'Y' and isRepImg = 'Y'";
        int getTeamRepImgParams = teamIdx;
        return this.jdbcTemplate.queryForObject(getTeamRepImgQuery,
                (rs, rsNum) -> new TeamRepImg(
                        rs.getString("fileName"),
                        rs.getString("teamFileUrl")),
                getTeamRepImgParams);
    }

    public List<GetTeamLikingRes> getTeamLikings(int userIdx){
        String getTeamLikingsQuery = "select teamIdx, userIdx, title, type, region, interests\n" +
                "from Team\n" +
                "inner join TeamLike TL on Team.teamIdx = TL.likingIdx\n" +
                "where likerIdx = ? and TL.status = 'active'\n" +
                "order by TL.updatedAt DESC";
        int getTeamLikingsParams = userIdx;

        return this.jdbcTemplate.query(getTeamLikingsQuery,
                (rs, rsNum) -> new GetTeamLikingRes(
                        rs.getInt("teamIdx"),
                        rs.getInt("userIdx"),
                        getTeamRepImg(rs.getInt("teamIdx")),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("region"),
                        rs.getString("interests")),
                getTeamLikingsParams);
    }

    public List<UserInfo> getUserInfo(int teamIdx){
        String getUserInfoQuery = "select userIdx, nickName, userImgUrl, gender, ageRange, region, occupation, interests\n" +
                "from TeamLike\n" +
                "inner join User U on TeamLike.likerIdx = U.userIdx\n" +
                "where likingIdx = ?";
        int getUsrInfoParams = teamIdx;

        return this.jdbcTemplate.query(getUserInfoQuery,
                (rs, rsNum) -> new UserInfo(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests")),
                getUsrInfoParams);
    }

    public List<GetTeamLikerRes> getTeamLikers(int userIdx){
        String getTeamLikersQuery = "select teamIdx, title, type from Team where userIdx = ?";
        int getTeamLikersParams = userIdx;

        return this.jdbcTemplate.query(getTeamLikersQuery,
                (rs, rsNum) -> new GetTeamLikerRes(
                        rs.getInt("teamIdx"),
                        rs.getString("title"),
                        rs.getString("type"),
                        getUserInfo(rs.getInt("teamIdx"))),
                getTeamLikersParams);
    }

}
