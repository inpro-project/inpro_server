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
        String createUserQuery = "insert into User (idToken, email, nickname, userImgUrl, ageRange, gender) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{kakaoUser.getIdToken(), kakaoUser.getEmail(), kakaoUser.getNickName(), kakaoUser.getUserImgUrl(), kakaoUser.getAgeRange(), kakaoUser.getGender()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
    }

    public int checkIdToken(long idToken){
        String checkIdTokenQuery = "select exists(select idToken from User where idToken = ?)";
        long checkIdTokenParams = idToken;
        return this.jdbcTemplate.queryForObject(checkIdTokenQuery,
                int.class,
                checkIdTokenParams);
    }

    public User getUser(long idToken){
        String getUserQuery = "select userIdx, idToken, email, nickname, userImgUrl, ageRange, gender, status from User where idToken = ?";
        long getUserParams = idToken;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getLong("idToken"),
                        rs.getString("email"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("ageRange"),
                        rs.getString("gender"),
                        rs.getString("status")),
                getUserParams
        );
    }

}
