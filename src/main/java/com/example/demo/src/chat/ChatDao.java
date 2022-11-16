package com.example.demo.src.chat;

import com.example.demo.src.chat.model.GetChatMemberRes;
import com.example.demo.src.chat.model.GetChatMessageCountRes;
import com.example.demo.src.chat.model.GetChatMessageRes;
import com.example.demo.src.chat.model.GetChatRoomRes;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatDao {
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource){
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public List<GetChatRoomRes> getChatRoomOne(int chatRoomIdx) {
    String getChatRoomOneQuery = "select ChatRoom.chatRoomIdx, ChatRoom.name, ChatRoom.content\n" +
        "from ChatRoom\n" +
        "where ChatRoom.chatRoomIdx = ? and CM.status = 'active'";

    int getChatRoomOneParams = chatRoomIdx;

    return this.jdbcTemplate.query(getChatRoomOneQuery,
        (rs, reNum) -> new GetChatRoomRes(
            rs.getInt("chatRoomIdx"),
            rs.getString("name"),
            rs.getString("content")),
        getChatRoomOneParams);
  }

  public List<GetChatRoomRes> getChatRoom(int userIdx){
    String getChatRoomQuery = "select ChatRoom.chatRoomIdx, ChatRoom.name, ChatRoom.content\n" +
        "from ChatRoom\n" +
        "inner join ChatMember CM on ChatRoom.chatRoomIdx = CM.chatRoomIdx\n" +
        "where CM.userIdx = ? and CM.status = 'active'\n" +
        "order by ChatRoom.createdAt";

    int getChatRoomParams = userIdx;

    return this.jdbcTemplate.query(getChatRoomQuery,
        (rs, reNum) -> new GetChatRoomRes(
            rs.getInt("chatRoomIdx"),
            rs.getString("name"),
            rs.getString("content")),
        getChatRoomParams);
  }

  public List<GetChatMessageRes> getChatRoomInfo(int chatRoomIdx) {
    String getChatMessageQuery = "select chatMessageIdx, userIdx, U.nickName, U.userImgUrl, chatMessage\n" +
        "from ChatMessage\n" +
        "inner join User U on ChatMessage.userIdx = U.userIdx\n" +
        "where ChatMessage.chatRoomIdx = ?\n" +
        "order by ChatMessage.createdAt";

    int getChatMessageParams = chatRoomIdx;

    return this.jdbcTemplate.query(getChatMessageQuery,
        (rs, reNum) -> new GetChatMessageRes(
            rs.getInt("chatMessageIdx"),
            rs.getInt("userIdx"),
            rs.getString("nickName"),
            rs.getString("userImgUrl"),
            rs.getString("chatMessage")),
        getChatMessageParams);
  }

  public int createChatRoom(int userIdx, String chatRoomName, String chatRoomContent) {
    String createChatRoomQuery = "insert into ChatRoom (userIdx, name, content) VALUES (?, ?, ?);";
    Object[] createChatRoomParams = new Object[]{userIdx, chatRoomName, chatRoomContent};
    this.jdbcTemplate.update(createChatRoomQuery, createChatRoomParams);

    String lastInsertIdQuery = "select last_insert_id()";
    return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
  }

  public int createChatMessage(int chatRoomIdx, int userIdx, String chatMessage) {
    String createChatMessageQuery = "insert into ChatMessage (chatRoomIdx, userIdx, chatMessage) VALUES (?, ?, ?);";
    Object[] createChatMessageParams = new Object[]{chatRoomIdx, userIdx, chatMessage};
    this.jdbcTemplate.update(createChatMessageQuery, createChatMessageParams);

    String lastInsertIdQuery = "select last_insert_id()";
    return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
  }

  public int deleteChatRoom(int chatRoomIdx) {
    String deleteChatRoomQuery = "update ChatRoom set status = 'deleted' where chatRoomIdx = ?";
    int deleteChatRoomParams = chatRoomIdx;
    return this.jdbcTemplate.update(deleteChatRoomQuery, deleteChatRoomParams);
  }

  public List<GetChatMemberRes> getChatMember(int chatRoomIdx){
    String getChatMemberQuery = "select chatMemberIdx, userIdx, U.nickName, U.userImgUrl\n" +
        "from ChatMember\n" +
        "inner join User U on ChatMember.userIdx = U.userIdx\n" +
        "where ChatMember.chatRoomIdx = ? and ChatMember.status = 'active'\n" +
        "order by ChatMember.createdAt";

    int getChatMemberParams = chatRoomIdx;

    return this.jdbcTemplate.query(getChatMemberQuery,
        (rs, reNum) -> new GetChatMemberRes(
            rs.getInt("chatMemberIdx"),
            rs.getInt("userIdx"),
            rs.getString("nickName"),
            rs.getString("userImgUrl")),
        getChatMemberParams);
  }

  public List<GetChatMessageCountRes> getChatMessageCount(int chatRoomIdx) {
    String getChatMessageQuery = "select ChatMessage.chatMessageIdx, ChatMessage.chatMessage\n" +
        "from ChatMessage\n" +
        "where ChatMessage.chatRoomIdx = ?\n" +
        "order by ChatMessage.createdAt";

    int getChatMessageParams = chatRoomIdx;

    return this.jdbcTemplate.query(getChatMessageQuery,
        (rs, reNum) -> new GetChatMessageCountRes(
            rs.getInt("chatMessageIdx"),
            rs.getString("chatMessage")),
        getChatMessageParams);
  }

  public List<GetChatMessageRes> getChatMessageByIdx(int chatRoomIdx, int chatMessageIdx) {
    String getChatMessageQuery = "select ChatMessage.chatMessageIdx, ChatMessage.userIdx, U.nickName, U.userImgUrl, ChatMessage.chatMessage\n" +
        "from ChatMessage\n" +
        "inner join User U on ChatMessage.userIdx = U.userIdx\n" +
        "where ChatMessage.chatRoomIdx = ? and ChatMessage.chatMessageIdx < ?\n" +
        "order by ChatMessage.createdAt\n" +
        "limit 20";

    Object[] getChatMessageParams = new Object[]{chatRoomIdx, chatMessageIdx};

    return this.jdbcTemplate.query(getChatMessageQuery,
        (rs, reNum) -> new GetChatMessageRes(
            rs.getInt("chatMessageIdx"),
            rs.getInt("userIdx"),
            rs.getString("nickName"),
            rs.getString("userImgUrl"),
            rs.getString("chatMessage")),
        getChatMessageParams);
  }

  public int createChatMember(int chatRoomIdx, int userIdx) {
    String createChatMemberQuery = "insert into ChatMember (chatRoomIdx, userIdx) VALUES (?, ?);";
    Object[] createChatMemberParams = new Object[]{chatRoomIdx, userIdx};
    this.jdbcTemplate.update(createChatMemberQuery, createChatMemberParams);

    String lastInsertIdQuery = "select last_insert_id()";
    return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
  }

  public int deleteChatMember(int chatMemberIdx){
    String deleteChatMemberQuery = "update ChatMember set status = 'deleted' where chatMemberIdx = ?";
    int deleteChatMemberParams = chatMemberIdx;
    return this.jdbcTemplate.update(deleteChatMemberQuery, deleteChatMemberParams);
  }

  public int checkChatMember(int chatRoomIdx, int userIdx){
    String checkChatMemberQuery = "select exists (select chatRoomIdx from chatMember where chatRoomIdx = ? and userIdx = ? and status = 'active')";
    Object[] checkChatMemberParams = new Object[] {chatRoomIdx, userIdx};

    return this.jdbcTemplate.queryForObject(checkChatMemberQuery, int.class, checkChatMemberParams);
  }

}
