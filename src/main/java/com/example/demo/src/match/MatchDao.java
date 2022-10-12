package com.example.demo.src.match;

import com.example.demo.src.match.model.GetLikerRes;
import com.example.demo.src.match.model.GetLikingRes;
import com.example.demo.src.user.model.GetProfileRes;
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

    public List<GetLikingRes> getLikings(int userIdx){
        String getLikingsQuery = "select userIdx as likingIdx, nickName, userImgUrl\n" +
                "     , case\n" +
                "         when gender = 'female' then '여'\n" +
                "         when gender = 'male' then '남'\n" +
                "         else '없음' end as gender\n" +
                "     , case\n" +
                "         when ageRange = '10~19' then '10대'\n" +
                "         when ageRange = '20~29' then '20대'\n" +
                "         when ageRange = '30~39' then '30대'\n" +
                "         when ageRange = '40~49' then '40대'\n" +
                "         when ageRange = '50~59' then '50대'\n" +
                "         when ageRange = '60~69' then '60대'\n" +
                "        else '없음' end as ageRange\n" +
                "     , region, occupation, job, interests\n" +
                "from User\n" +
                "inner join UserLike UL on User.userIdx = UL.likingIdx\n" +
                "where likerIdx = ? and UL.status = 'active'";
        int getLikingsParams = userIdx;

        return this.jdbcTemplate.query(getLikingsQuery,
                (rs, rsNum) -> new GetLikingRes(
                        rs.getInt("likingIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("job"),
                        rs.getString("interests")),
                getLikingsParams);
    }

    public List<GetLikerRes> getLikers(int userIdx){
        String getLikersQuery = "select userIdx as likerIdx, nickName, userImgUrl\n" +
                "     , case\n" +
                "         when gender = 'female' then '여'\n" +
                "         when gender = 'male' then '남'\n" +
                "         else '없음' end as gender\n" +
                "     , case\n" +
                "         when ageRange = '10~19' then '10대'\n" +
                "         when ageRange = '20~29' then '20대'\n" +
                "         when ageRange = '30~39' then '30대'\n" +
                "         when ageRange = '40~49' then '40대'\n" +
                "         when ageRange = '50~59' then '50대'\n" +
                "         when ageRange = '60~69' then '60대'\n" +
                "        else '없음' end as ageRange\n" +
                "     , region, occupation, job, interests\n" +
                "from User\n" +
                "inner join UserLike UL on User.userIdx = UL.likerIdx\n" +
                "where likingIdx = ? and UL.status = 'active'";
        int getLikersParams = userIdx;

        return this.jdbcTemplate.query(getLikersQuery,
                (rs, rsNum) -> new GetLikerRes(
                        rs.getInt("likerIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("job"),
                        rs.getString("interests")),
                getLikersParams);
    }

}
