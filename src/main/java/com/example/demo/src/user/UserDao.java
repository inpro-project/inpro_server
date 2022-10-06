package com.example.demo.src.user;

import com.example.demo.src.user.model.PatchPortfolioReq;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostPortfolioReq;
import com.example.demo.src.user.model.PostPortfolioRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int modifyUser(int userIdx, String storeFileUrl, PatchUserReq patchUserReq){
        String modifyUserQuery = "update User set nickName = ?, userImgUrl = ?, comment = ?, region = ?, occupation = ?, job = ?, interests = ? where userIdx = ?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getNickName(), storeFileUrl, patchUserReq.getComment(), patchUserReq.getRegion(),
                patchUserReq.getOccupation(), patchUserReq.getJob(), patchUserReq.getInterests(), userIdx};
        return this.jdbcTemplate.update(modifyUserQuery, modifyUserParams);
    }

    public int createPortfolio(int userIdx, int portfolioCategoryIdx, PostPortfolioReq postPortfolioReq){
        String createPortfolioQuery = "insert into Portfolio (userIdx, portfolioCategoryIdx, title, content, url) VALUES (?, ?, ?, ?, ?);";
        Object[] createPortfolioParams = new Object[]{userIdx, portfolioCategoryIdx, postPortfolioReq.getTitle(), postPortfolioReq.getContent(), postPortfolioReq.getUrl()};
        this.jdbcTemplate.update(createPortfolioQuery, createPortfolioParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkPortfolioIdx(int userIdx, int portfolioIdx){
        String checkPortfolioIdxQuery = "select exists (select portfolioIdx from Portfolio where userIdx = ? and portfolioIdx = ?)";
        Object[] checkPortfolioIdxParams = new Object[] {userIdx, portfolioIdx};

        return this.jdbcTemplate.queryForObject(checkPortfolioIdxQuery, int.class, checkPortfolioIdxParams);
    }

    public int updatePortfolio(int portfolioIdx, PatchPortfolioReq patchPortfolioReq){
        String updatePortfolioQuery = "update Portfolio set title = ?, content = ?, url = ? where portfolioIdx = ?";
        Object[] updatePortfolioParams = new Object[]{patchPortfolioReq.getTitle(), patchPortfolioReq.getContent(), patchPortfolioReq.getUrl(), portfolioIdx};
        return this.jdbcTemplate.update(updatePortfolioQuery, updatePortfolioParams);
    }


}
