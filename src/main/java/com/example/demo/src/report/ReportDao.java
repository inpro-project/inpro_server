package com.example.demo.src.report;

import com.example.demo.src.report.model.GetBlockedUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReportDao {
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

    public int checkPreBlock(int userIdx, int blockedUserIdx){
        String checkPreBlockQuery = "select exists(select blockIdx from Block where blockUserIdx = ? and blockedUserIdx = ? and status = 'active')";
        Object[] checkPreBlockParams = new Object[]{userIdx, blockedUserIdx};
        return this.jdbcTemplate.queryForObject(checkPreBlockQuery,
                int.class,
                checkPreBlockParams);
    }

    public int checkBlockHist(int userIdx, int blockedUserIdx){
        String checkBlockHistQuery = "select case\n" +
                "        when COUNT(*) = 0 then 0\n" +
                "        else blockIdx end as blockIdx\n" +
                "from Block\n" +
                "where blockUserIdx = ?  and blockedUserIdx = ? and status in ('inactive', 'deleted')";
        Object[] checkBlockHistParams = new Object[]{userIdx, blockedUserIdx};
        return this.jdbcTemplate.queryForObject(checkBlockHistQuery,
                int.class,
                checkBlockHistParams);
    }

    public int updateBlock(int blockIdx){
        String updateBlockQuery = "update Block set status = 'active' where blockIdx = ?";
        int updateBlockParams = blockIdx;
        return this.jdbcTemplate.update(updateBlockQuery, updateBlockParams);
    }

    public int createBlock(int userIdx, int blockedUserIdx) {
        String createBlockQuery = "insert into Block (blockUserIdx, blockedUserIdx) VALUES (?, ?)";
        Object[] createBlockParams = new Object[]{userIdx, blockedUserIdx};
        this.jdbcTemplate.update(createBlockQuery, createBlockParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int deleteBlock(int userIdx, int blockedUserIdx){
        String deleteBlockQuery = "update Block set status = 'deleted' where blockUserIdx = ? and blockedUserIdx = ?";
        Object[] deleteBlockParams = new Object[]{userIdx, blockedUserIdx};

        return this.jdbcTemplate.update(deleteBlockQuery, deleteBlockParams);
    }

    public List<GetBlockedUserRes> getBlockedUsers(int userIdx){
        String getBlockedUsersQuery = "select blockIdx, blockedUserIdx, nickName, userImgUrl, gender, ageRange\n" +
                "from Block\n" +
                "inner join User U on Block.blockedUserIdx = U.userIdx\n" +
                "where blockUserIdx = ? and Block.status = 'active' order by Block.updatedAt desc";
        int getBlockedUsersParams = userIdx;

        return this.jdbcTemplate.query(getBlockedUsersQuery,
                (rs, rsNum) -> new GetBlockedUserRes(
                        rs.getInt("blockIdx"),
                        rs.getInt("blockedUserIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImgUrl"),
                        rs.getString("gender"),
                        rs.getString("ageRange")),
                getBlockedUsersParams);
    }

}
