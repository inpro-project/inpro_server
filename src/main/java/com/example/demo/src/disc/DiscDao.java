package com.example.demo.src.disc;

import com.example.demo.src.disc.model.GetDiscTestRes;
import com.example.demo.src.oauth.model.KakaoUser;
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

    public int createUserDisc(int userIdx, double x, double y, String isRepDisc){
        String createUserDiscQuery = "insert into UserDisc (userIdx, x, y, isRepDisc) VALUES (?, ?, ?, ?);";
        Object[] createUserDiscParams = new Object[]{userIdx, x, y, isRepDisc};
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

    public int createSearchDisc(int userIdx, double x, double y, String isRepDisc){
        String createSearchDiscQuery = "insert into SearchDisc (userIdx, x, y, isRepDisc) VALUES (?, ?, ?, ?);";
        Object[] createSearchDiscParams = new Object[]{userIdx, x, y, isRepDisc};
        this.jdbcTemplate.update(createSearchDiscQuery, createSearchDiscParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void createSearchDiscTestAsGood(int searchDiscIdx, int discFeatureIdx){
        String createSearchDiscTestAsGoodQuery = "insert into SearchDiscTest (searchDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createSearchDiscTestAsGoodParams = new Object[]{searchDiscIdx, discFeatureIdx, "Y"};
        this.jdbcTemplate.update(createSearchDiscTestAsGoodQuery, createSearchDiscTestAsGoodParams);
    }

    public void createSearchDiscTestAsBad(int searchDiscIdx, int discFeatureIdx){
        String createSearchDiscTestAsBadQuery = "insert into SearchDiscTest (searchDiscIdx, discFeatureIdx, fitOrNot) VALUES (?, ?, ?);";
        Object[] createSearchDiscTestAsBadParams = new Object[]{searchDiscIdx, discFeatureIdx, "N"};
        this.jdbcTemplate.update(createSearchDiscTestAsBadQuery, createSearchDiscTestAsBadParams);
    }

    public int checkUserDisc(int userIdx){
        String checkUserDiscQuery = "select exists(select userDiscIdx from UserDisc where userIdx = ? and status = 'active')";
        int checkUserDiscParams = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserDiscQuery, int.class, checkUserDiscParams);
    }

    public int checkSearchDisc(int userIdx){
        String checkSearchDiscQuery = "select exists(select searchDiscIdx from SearchDisc where userIdx = ? and status = 'active')";
        int checkSearchDiscParams = userIdx;

        return this.jdbcTemplate.queryForObject(checkSearchDiscQuery, int.class, checkSearchDiscParams);
    }

}
