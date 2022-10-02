package com.example.demo.src.oauth;

import com.example.demo.src.oauth.model.KakaoUser;
import com.example.demo.src.oauth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class KakaoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createUser(KakaoUser kakaoUser){
        String createUserQuery = "insert into User (email, nickname, userImgUrl, age_range, gender) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{kakaoUser.getEmail(), kakaoUser.getNickName(), kakaoUser.getUserImgUrl(), kakaoUser.getAge_range(), kakaoUser.getGender()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public User getUser(String email){
        String getUserQuery = "select userIdx, email, nickname, userImgUrl, age_range, gender, status from User where email = ?";
        String getUserParams = email;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("age_range"),
                        rs.getString("gender"),
                        rs.getString("status")),
                getUserParams
        );
    }

}
