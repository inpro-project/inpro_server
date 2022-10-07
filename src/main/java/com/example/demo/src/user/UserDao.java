package com.example.demo.src.user;

import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    public int deletePortfolio(int portfolioIdx){
        String deletePortfolioQuery = "update Portfolio set status = 'deleted' where portfolioIdx = ?";
        int deletePortfolioParams = portfolioIdx;
        return this.jdbcTemplate.update(deletePortfolioQuery, deletePortfolioParams);
    }

    public List<GetPortfolioRes> getMyPortfolios(int userIdx, int portfolioCategoryIdx) {
        String getMyPortfoliosQuery = "select portfolioIdx, title, content, url, isRepPortfolio\n" +
                "from Portfolio\n" +
                "where userIdx = ? and portfolioCategoryIdx = ? and status = 'active'";
        Object[] getMyPortfoliosParams = new Object[]{userIdx, portfolioCategoryIdx};

        return this.jdbcTemplate.query(getMyPortfoliosQuery,
                (rs, rsNum) -> new GetPortfolioRes(
                        rs.getInt("portfolioIdx"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("url"),
                        rs.getString("isRepPortfolio")),
                getMyPortfoliosParams);
    }

    public int createUserTags(int userIdx, String name){
        String createUserTagsQuery = "insert into UserTag (userIdx, name) VALUES (?, ?);";
        Object[] createUserTagsParams = new Object[]{userIdx, name};
        this.jdbcTemplate.update(createUserTagsQuery, createUserTagsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int getNumOfUserTag(int userIdx){
        String getNumOfUserTagQuery = "select COUNT(*) from UserTag where userIdx = ?";
        int getNumOfUserTagParams = userIdx;

        return this.jdbcTemplate.queryForObject(getNumOfUserTagQuery, int.class, userIdx);
    }

}
