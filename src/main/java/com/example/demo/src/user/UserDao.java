package com.example.demo.src.user;

import com.example.demo.src.user.model.GetUserMatchRes;
import com.example.demo.src.match.model.SearchDiscXy;
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
        String updatePortfolioQuery = "update Portfolio set title = ?, content = ?, url = ?, isRepPortfolio = ? where portfolioIdx = ?";
        Object[] updatePortfolioParams = new Object[]{patchPortfolioReq.getTitle(), patchPortfolioReq.getContent(), patchPortfolioReq.getUrl(), patchPortfolioReq.getIsRepPortfolio(), portfolioIdx};
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

    public List<DiscFeature> getDiscFeatures(int userIdx){
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
                (rs, rsNum) -> new DiscFeature(
                        rs.getInt("discFeatureIdx"),
                        rs.getString("feature")),
                getDiscFeaturesParams);
    }

    public List<UserTag> getUserTags(int userIdx){
        String getUserTagsQuery = "select userTagIdx, name\n" +
                "from UserTag\n" +
                "where userIdx = ? and status = 'active'\n" +
                "order by userTagIdx";
        int getUserTagsParams = userIdx;

        return this.jdbcTemplate.query(getUserTagsQuery,
                (rs, rsNum) -> new UserTag(
                        rs.getInt("userTagIdx"),
                        rs.getString("name")),
                getUserTagsParams);
    }

    public List<RepPortfolio> getRepPortfolios(int userIdx){
        String getRepPortfoliosQuery = "select portfolioCategoryIdx, title\n" +
                "from Portfolio\n" +
                "where userIdx = ? and isRepPortfolio = 'Y' and status = 'active'\n" +
                "order by portfolioCategoryIdx";
        int getRepPortfoliosParams = userIdx;

        return this.jdbcTemplate.query(getRepPortfoliosQuery,
                (rs, rsNum) -> new RepPortfolio(
                        rs.getInt("portfolioCategoryIdx"),
                        rs.getString("title")),
                getRepPortfoliosParams);
    }

    public int checkUserDisc(int userIdx){
        String checkUserDiscQuery = "select exists(select * from UserDisc where userIdx = ? and status = 'active' and isRepDisc = 'Y')";
        int checkUserDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserDiscQuery, int.class, checkUserDiscParams);
    }

    public UserDisc getUserDisc(int userIdx){
        String getUserDiscQuery = "select x, y from UserDisc\n" +
                "where userIdx = ? and status = 'active' and isRepDisc = 'Y'";
        int getUserDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserDiscQuery,
                (rs, rsNum) -> new UserDisc(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getUserDiscParams);
    }

    public GetMyProfileRes getMyProfile(int userIdx, double x, double y){
        String getUserProfileQuery = "select nickName, userImgUrl\n" +
                ", gender, ageRange, comment, region, occupation, interests\n" +
                "from User\n" +
                "where userIdx = ?";
        int getUserProfileParams = userIdx;

        return this.jdbcTemplate.queryForObject(getUserProfileQuery,
                (rs, rsNum) -> new GetMyProfileRes(
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("comment"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests"),
                        x, y,
                        getDiscFeatures(userIdx),
                        getUserTags(userIdx),
                        getRepPortfolios(userIdx)),
                getUserProfileParams);
    }

    public GetUserProfileRes getUserProfile(double x, double y, int userIdx){
        String getUserProfileQuery = "select nickName, userImgUrl, gender, ageRange, comment, region, occupation, interests\n" +
                "    , case when x is not null then x else 0 end as x\n" +
                "    , case when y is not null then y else 0 end as y\n" +
                "    , case when x is not null and ? is not null\n" +
                "         then ROUND(100 - SQRT(POWER(x - (?), 2) + POWER(y - (?), 2)) / 33.07752975109958 * 100)\n" +
                "        else 0 end as percent\n" +
                "from User\n" +
                "left join UserDisc UD on User.userIdx = UD.userIdx and UD.status = 'active' and isRepDisc = 'Y'\n" +
                "where User.userIdx = ?";
        Object[] getUserProfileParams = new Object[]{x, x, y, userIdx};

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
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getInt("percent"),
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

    public List<String> getUserFilter(int userIdx, int category) {
        String getUserFilterQuery = "select name from UserFilter where userIdx = ? and category = ? and status = 'active'";
        Object[] getUserFilterParams = new Object[]{userIdx, category};

        return this.jdbcTemplate.query(getUserFilterQuery,
                (rs, rsNum) -> (rs.getString("name")),
                getUserFilterParams);
    }

    public int checkSearchDisc(int userIdx){
        String checkSearchDiscQuery = "select exists(select * from SearchDisc where userIdx = ?)";
        int checkSearchDiscParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkSearchDiscQuery,
                int.class,
                checkSearchDiscParams);
    }

    public SearchDiscXy getPastSearchDisc(int userIdx) {
        String getPastSearchDiscQuery = "select x, y from SearchDisc where userIdx = ?";
        int getPastSearchDiscParams = userIdx;
        return jdbcTemplate.queryForObject(getPastSearchDiscQuery,
                (rs, rsNum) -> new SearchDiscXy(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getPastSearchDiscParams);
    }

    public SearchDiscXy getNewSearchDisc(int userIdx) {
        String getNewSearchDiscQuery = "select case when COUNT(*) > 5 then AVG(x) else null end as x\n" +
                "    , case when COUNT(*) > 5 then AVG(y) else null end as y\n" +
                "from UserLike\n" +
                "inner join User U on UserLike.likingIdx = U.userIdx and U.status = 'active'\n" +
                "inner join UserDisc UD on U.userIdx = UD.userIdx and isRepDisc = 'Y' and UD.status = 'active'\n" +
                "where likerIdx = ? and UserLike.status = 'active'";
        int getNewSearchDiscParams = userIdx;
        return jdbcTemplate.queryForObject(getNewSearchDiscQuery,
                (rs, rsNum) -> new SearchDiscXy(
                        rs.getDouble("x"),
                        rs.getDouble("y")),
                getNewSearchDiscParams);
    }

    public void createSearchDisc(int userIdx, double x, double y){
        String createSearchDiscQuery = "insert into SearchDisc (userIdx, x, y) values (?, ?, ?)";
        Object[] createSearchDiscParams = new Object[]{userIdx, x, y};
        this.jdbcTemplate.update(createSearchDiscQuery, createSearchDiscParams);
    }

    public int updateSearchDisc(int userIdx, double x, double y){
        String updateSearchDiscQuery = "update SearchDisc set x = ?, y = ? where userIdx = ?";
        Object[] updateSearchDiscParams = new Object[]{x, y, userIdx};
        return this.jdbcTemplate.update(updateSearchDiscQuery, updateSearchDiscParams);
    }

    public List<GetUserMatchRes> getUserMatches(int userIdx, SearchDiscXy searchDiscXy, List<String> ageRange, List<String> region, List<String> occupation, List<String> interests) {
        String getUserMatchesQuery = "select User.userIdx, nickName, userImgUrl\n" +
                "     , gender, ageRange, comment, region, occupation, interests\n" +
                "     , case when x is not null then x else 0 end as x\n" +
                "     , case when y is not null then y else 0 end as y\n" +
                "     , case when x is not null and ? is not null\n" +
                "         then ROUND(100 - SQRT(POWER(x - (?), 2) + POWER(y - (?), 2)) / 33.07752975109958 * 100)\n" +
                "        else 0 end as percent\n" +
                "from User\n" +
                "left join UserDisc UD on User.userIdx = UD.userIdx and UD.status = 'active' and isRepDisc = 'Y'\n" +
                "where User.userIdx not in(?)\n" +
                "  and User.userIdx not in (select passingIdx from UserPass where passerIdx = ? and UserPass.status = 'active')\n" +
                "  and User.userIdx not in (select blockedUserIdx from Block where blockUserIdx = ? and Block.status = 'active')\n" +
                "  and User.userIdx not in (select reportedUserIdx from Report where Report.userIdx = ?)\n" +
                "  and User.userIdx not in (select likingIdx from UserLike where likerIdx = ? and UserLike.status = 'active')\n" +
                "and User.status = 'active'\n" +
                "and ageRange in (?, ?, ?, ?, ?, ?)\n" +
                "  and ageRange not in (' ')\n" +
                "and region in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "  and region not in (' ')\n" +
                "and occupation in (?, ?, ?, ?, ?)\n" +
                "  and occupation not in (' ')\n" +
                "and interests in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                "  and interests not in (' ')\n" +
                "order by percent DESC, userIdx DESC";

        Object[] getUserMatchesParams = new Object[]{searchDiscXy.getX(), searchDiscXy.getX(), searchDiscXy.getY(), userIdx, userIdx, userIdx, userIdx, userIdx
                , ageRange.get(0), ageRange.get(1), ageRange.get(2), ageRange.get(3), ageRange.get(4), ageRange.get(5)
                , region.get(0), region.get(1), region.get(2), region.get(3), region.get(4), region.get(5), region.get(6), region.get(7)
                , region.get(8), region.get(9), region.get(10), region.get(11), region.get(12), region.get(13), region.get(14), region.get(15), region.get(16)
                , occupation.get(0), occupation.get(1), occupation.get(2), occupation.get(3), occupation.get(4)
                , interests.get(0), interests.get(1), interests.get(2), interests.get(3), interests.get(4), interests.get(5)
                , interests.get(6), interests.get(7), interests.get(8), interests.get(9), interests.get(10), interests.get(11), interests.get(12), interests.get(13)};

        return this.jdbcTemplate.query(getUserMatchesQuery,
                (rs, rsNum) -> new GetUserMatchRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange"),
                        rs.getString("comment"),
                        rs.getString("region"),
                        rs.getString("occupation"),
                        rs.getString("interests"),
                        rs.getDouble("x"),
                        rs.getDouble("y"),
                        rs.getInt("percent"),
                        getDiscFeatures(rs.getInt("userIdx")),
                        getUserTags(rs.getInt("userIdx")),
                        getRepPortfolios(rs.getInt("userIdx"))),
                getUserMatchesParams);
    }

}
