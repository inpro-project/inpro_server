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

    public int modifyUser(int userIdx, PatchUserReq patchUserReq){
        String modifyUserQuery = "update User set nickName = ?, comment = ?, region = ?, occupation = ?, interests = ? where userIdx = ?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getComment(), patchUserReq.getRegion(),
                patchUserReq.getOccupation(), patchUserReq.getInterests(), userIdx};
        return this.jdbcTemplate.update(modifyUserQuery, modifyUserParams);
    }

    public int modifyUserProfileImg(int userIdx, String storeFileUrl){
        String modifyUserProfileImgQuery = "update User set userImgUrl = ? where userIdx = ?";
        Object[] modifyUserProfileImgParams = new Object[]{storeFileUrl, userIdx};
        return this.jdbcTemplate.update(modifyUserProfileImgQuery, modifyUserProfileImgParams);
    }

    public int createPortfolio(int userIdx, int portfolioCategoryIdx, PostPortfolioReq postPortfolioReq, String isRepPortfolio){
        String createPortfolioQuery = "insert into Portfolio (userIdx, portfolioCategoryIdx, title, content, url, isRepPortfolio) VALUES (?, ?, ?, ?, ?, ?);";
        Object[] createPortfolioParams = new Object[]{userIdx, portfolioCategoryIdx, postPortfolioReq.getTitle(), postPortfolioReq.getContent(), postPortfolioReq.getUrl(), isRepPortfolio};
        this.jdbcTemplate.update(createPortfolioQuery, createPortfolioParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkPortfolioIdx(int userIdx, int portfolioIdx){
        String checkPortfolioIdxQuery = "select exists (select portfolioIdx from Portfolio where userIdx = ? and portfolioIdx = ? and status = 'active')";
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

    public int checkUserIdx(int userIdx){
        String checkUserIdxQuery = "select exists(select * from User where userIdx = ? and status = 'active')";
        int checkUserIdxParams = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkUserIdxParams);
    }

    public List<GetPortfolioRes> getPortfolios(int userIdx, int portfolioCategoryIdx) {
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
        String getNumOfUserTagQuery = "select COUNT(*) from UserTag where userIdx = ? and status = 'active'";
        int getNumOfUserTagParams = userIdx;

        return this.jdbcTemplate.queryForObject(getNumOfUserTagQuery, int.class, getNumOfUserTagParams);
    }

    public int checkUserTagIdx(int userIdx, int userTagIdx){
        String checkUserTagIdxQuery = "select exists(select userTagIdx from UserTag where userIdx = ? and userTagIdx = ? and status = 'active')";
        Object[] checkUserTagIdxParams = new Object[] {userIdx, userTagIdx};

        return this.jdbcTemplate.queryForObject(checkUserTagIdxQuery, int.class, checkUserTagIdxParams);
    }

    public int deleteUserTag(int userTagIdx){
        String deleteUserTagQuery = "update UserTag set status = 'deleted' where userTagIdx = ?";
        int deleteUserTagParams = userTagIdx;
        return this.jdbcTemplate.update(deleteUserTagQuery, deleteUserTagParams);
    }

    public List<GetUserDiscRes> getUserDisc(int userIdx){
        String getUserDiscQuery = "select userDiscIdx, x, y\n" +
                "from UserDisc\n" +
                "where userIdx = ? and status = 'active' and isRepDisc = 'Y'";
        int getUserDiscParams = userIdx;

        return this.jdbcTemplate.query(getUserDiscQuery,
                (rs, rsNum) -> new GetUserDiscRes(
                        rs.getInt("userDiscIdx"),
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getUserDiscParams);
    }

    public List<GetDiscFeatureRes> getDiscFeatures(int userIdx){
        String getDiscFeaturesQuery = "select discFeatureIdx, feature\n" +
                "from UserDisc\n" +
                "inner join (select UserDiscTest.userDiscIdx, UserDiscTest.discFeatureIdx, DF.feature\n" +
                "            from UserDiscTest\n" +
                "            inner join DiscFeature DF on UserDiscTest.discFeatureIdx = DF.discFeatureIdx\n" +
                "            where fitOrNot = 'Y') as UDT on UserDisc.userDiscIdx = UDT.userDiscIdx\n" +
                "where userIdx = ? and UserDisc.status = 'active' and isRepDisc = 'Y'\n" +
                "order by rand()\n" +
                "limit 3";
        int getDiscFeaturesParams = userIdx;

        return this.jdbcTemplate.query(getDiscFeaturesQuery,
                (rs, rsNum) -> new GetDiscFeatureRes(
                        rs.getInt("discFeatureIdx"),
                        rs.getString("feature")),
                getDiscFeaturesParams);
    }

    public List<GetUserTagRes> getUserTags(int userIdx){
        String getUserTagsQuery = "select userTagIdx, name\n" +
                "from UserTag\n" +
                "where userIdx = ? and status = 'active'\n" +
                "order by userTagIdx";
        int getUserTagsParams = userIdx;

        return this.jdbcTemplate.query(getUserTagsQuery,
                (rs, rsNum) -> new GetUserTagRes(
                        rs.getInt("userTagIdx"),
                        rs.getString("name")),
                getUserTagsParams);
    }

    public List<GetRepPortfolioRes> getRepPortfolios(int userIdx){
        String getRepPortfoliosQuery = "select portfolioCategoryIdx, title\n" +
                "from Portfolio\n" +
                "where userIdx = ? and isRepPortfolio = 'Y' and status = 'active'\n" +
                "order by portfolioCategoryIdx";
        int getRepPortfoliosParams = userIdx;

        return this.jdbcTemplate.query(getRepPortfoliosQuery,
                (rs, rsNum) -> new GetRepPortfolioRes(
                        rs.getInt("portfolioCategoryIdx"),
                        rs.getString("title")),
                getRepPortfoliosParams);
    }

    public GetUserProfileRes getUserProfile(int userIdx){
        String getUserProfileQuery = "select nickName, userImgUrl\n" +
                ", gender, ageRange, comment, region, occupation, interests\n" +
                "from User\n" +
                "where userIdx = ?";
        int getUserProfileParams = userIdx;

        return this.jdbcTemplate.queryForObject(getUserProfileQuery,
                (rs, rsNum) -> new GetUserProfileRes(
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("comment"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests"),
                        getUserDisc(userIdx),
                        getDiscFeatures(userIdx),
                        getUserTags(userIdx),
                        getRepPortfolios(userIdx)),
                getUserProfileParams);
    }

    public int checkPortfolio(int userIdx, int portfolioCategoryIdx){
        String checkPortfolioQuery = "select exists (select portfolioIdx from Portfolio where userIdx = ? and portfolioCategoryIdx = ? and status = 'active')";
        Object[] checkPortfolioParams = new Object[] {userIdx, portfolioCategoryIdx};

        return this.jdbcTemplate.queryForObject(checkPortfolioQuery, int.class, checkPortfolioParams);
    }

    public int checkUser(int userIdx){
        String checkUserQuery = "select exists (select userIdx from User where userIdx = ? and status = 'active')";
        int checkUserParams = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserQuery, int.class, checkUserParams);
    }

}
