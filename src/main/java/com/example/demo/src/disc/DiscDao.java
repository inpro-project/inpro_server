package com.example.demo.src.disc;

import com.example.demo.src.disc.model.GetDiscTestRes;
import com.example.demo.src.disc.model.GetUserDiscResultRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DiscDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetDiscTestRes> getDiscTest(){
        String getDiscTestQuery = "select D.discIdx, name, discFeatureIdx, feature\n" +
                "from DiscFeature\n" +
                "inner join Disc D on DiscFeature.discIdx = D.discIdx\n" +
                "order by discFeatureIdx";

        return this.jdbcTemplate.query(getDiscTestQuery,
                (rs, rsNum) -> new GetDiscTestRes(
                        rs.getInt("discIdx"),
                        rs.getString("name"),
                        rs.getInt("discFeatureIdx"),
                        rs.getString("feature")
                ));
    }

    public int createUserDisc(int userIdx, double xy[], String isRepDisc, double discPercent[]){
        String createUserDiscQuery = "insert into UserDisc (userIdx, x, y, isRepDisc, dPercent, iPercent, sPercent, cPercent) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        Object[] createUserDiscParams = new Object[]{userIdx, xy[0], xy[1], isRepDisc, discPercent[0], discPercent[1], discPercent[2], discPercent[3]};
        this.jdbcTemplate.update(createUserDiscQuery, createUserDiscParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void createUserDiscTestAsGood(int userDiscIdx, int discFeatureIdx){
        String createUserDiscTestAsGoodQuery = "insert into UserDiscTest (userDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createUserDiscTestAsGoodParams = new Object[]{userDiscIdx, discFeatureIdx, "Y"};
        this.jdbcTemplate.update(createUserDiscTestAsGoodQuery, createUserDiscTestAsGoodParams);
    }

    public void createUserDiscTestAsBad(int userDiscIdx, int discFeatureIdx){
        String createUserDiscTestAsBadQuery = "insert into UserDiscTest (userDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createUserDiscTestAsBadParams = new Object[]{userDiscIdx, discFeatureIdx, "N"};
        this.jdbcTemplate.update(createUserDiscTestAsBadQuery, createUserDiscTestAsBadParams);
    }

    public int checkUserDisc(int userIdx){
        String checkUserDiscQuery = "select exists(select userDiscIdx from UserDisc where userIdx = ? and status = 'active')";
        int checkUserDiscParams = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserDiscQuery, int.class, checkUserDiscParams);
    }

    public int checkUserDiscIdx(int userIdx, int userDiscIdx){
        String checkUserDiscIdxQuery = "select exists(select userDiscIdx from UserDisc where userIdx = ? and userDiscIdx = ? and status = 'active')";
        Object[] checkUserDiscIdxParams = new Object[]{userIdx, userDiscIdx};

        return this.jdbcTemplate.queryForObject(checkUserDiscIdxQuery, int.class, checkUserDiscIdxParams);
    }

    public GetUserDiscResultRes getUserDiscResult(int userDiscIdx){
        String getUserDiscResultQuery = "select userDiscIdx, name, x, y, isRepDisc\n" +
                "     , ROUND(dPercent) as dPercent, ROUND(ipercent) as iPercent\n" +
                "     , ROUND(sPercent) as sPercent, ROUND(cPercent) as cPercent\n" +
                "from UserDisc\n" +
                "where userDiscIdx = ?";
        int getUserDiscResultParams = userDiscIdx;

        return this.jdbcTemplate.queryForObject(getUserDiscResultQuery,
                (rs, rsNum) -> new GetUserDiscResultRes(
                        rs.getInt("userDiscIdx"),
                        rs.getString("name"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getString("isRepDisc"),
                        rs.getInt("dPercent"),
                        rs.getInt("iPercent"),
                        rs.getInt("sPercent"),
                        rs.getInt("cPercent")),
                getUserDiscResultParams);
    }

    public int getUserDiscCount(int userIdx){
        String getUserDiscCountQuery = "select COUNT(*) as count from UserDisc where userIdx = ?";
        int getUserDiscCountParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserDiscCountQuery, int.class, getUserDiscCountParams);
    }

    public int updateUserDiscName(int userDiscIdx, String name){
        String updateUserNameQuery = "update UserDisc set name = ? where userDiscIdx = ?";
        Object[] updateUserNameParams = new Object[]{name, userDiscIdx};
        return this.jdbcTemplate.update(updateUserNameQuery, updateUserNameParams);
    }

    public List<GetUserDiscResultRes> getUserDiscResultList(int userIdx){
        String getUserDiscResultListQuery = "select userDiscIdx, name, x, y, isRepDisc\n" +
                "     , ROUND(dPercent) as dPercent, ROUND(ipercent) as iPercent\n" +
                "     , ROUND(sPercent) as sPercent, ROUND(cPercent) as cPercent\n" +
                "from UserDisc\n" +
                "where userIdx = ? and status = 'active'";
        int getUserDiscResultListParams = userIdx;

        return this.jdbcTemplate.query(getUserDiscResultListQuery,
                (rs, rsNum) -> new GetUserDiscResultRes(
                        rs.getInt("userDiscIdx"),
                        rs.getString("name"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getString("isRepDisc"),
                        rs.getInt("dPercent"),
                        rs.getInt("iPercent"),
                        rs.getInt("sPercent"),
                        rs.getInt("cPercent")),
                getUserDiscResultListParams);
    }

}
