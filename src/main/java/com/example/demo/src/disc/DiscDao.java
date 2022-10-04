package com.example.demo.src.disc;

import com.example.demo.src.disc.model.GetDiscTestRes;
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
        String getDiscTestQuery = "select discIdx, discFeatureIdx, feature from DiscFeature";

        return this.jdbcTemplate.query(getDiscTestQuery,
                (rs, rsNum) -> new GetDiscTestRes(
                        rs.getInt("discIdx"),
                        rs.getInt("discFeatureIdx"),
                        rs.getString("feature")
                ));
    }
}
