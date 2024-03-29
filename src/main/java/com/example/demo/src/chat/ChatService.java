package com.example.demo.src.chat;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.INVALID_USERIDX;
import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.GetChatMatchRoomRes;
import com.example.demo.src.chat.model.GetChatMemberRes;
import com.example.demo.src.chat.model.GetChatMessageCountRes;
import com.example.demo.src.chat.model.GetChatMessageRes;
import com.example.demo.src.chat.model.GetChatRoomAllRes;
import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.user.UserDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {
  private final ChatDao chatDao;
  private final UserDao userDao;

  public List<GetChatRoomAllRes> findAllRoom(int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    try {
      List<GetChatRoomRes> getChatRoomResList = chatDao.getChatRoom(userIdx);
      List<GetChatRoomAllRes> getChatRoomAllResList = new ArrayList<>();

      for (int i = 0; i < getChatRoomResList.size(); i++) {
        GetChatRoomRes getChatRoomRes = getChatRoomResList.get(i);
        int chatRoomIdx = getChatRoomRes.getChatRoomIdx();
        List<GetChatMessageCountRes> getChatMessageCountResList = chatDao.getChatMessageCount(chatRoomIdx);
        int messageCount = getChatMessageCountResList.size();
        String message = "";
        if (getChatMessageCountResList.isEmpty()) {
          GetChatRoomAllRes getChatRoomAllRes = new GetChatRoomAllRes(
              getChatRoomRes.getChatRoomIdx(), messageCount, getChatRoomRes.getName(), getChatRoomRes.getContent(), message, getChatRoomRes.getCreatedAt());
          getChatRoomAllResList.add(getChatRoomAllRes);
        }
        else {
          message = getChatMessageCountResList.get(0).getChatMessage();
          GetChatRoomAllRes getChatRoomAllRes = new GetChatRoomAllRes(
              getChatRoomRes.getChatRoomIdx(), messageCount, getChatRoomRes.getName(),
              getChatRoomRes.getContent(), message, getChatMessageCountResList.get(0).getCreatedAt());
          getChatRoomAllResList.add(getChatRoomAllRes);
        }
      }
      Collections.sort(getChatRoomAllResList);
      return getChatRoomAllResList;
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public int createRoom(int userIdx, String name, String content) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    try {
      return chatDao.createChatRoom(userIdx, name, content);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public GetChatRoomRes getRoomById(int chatRoomIdx, int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    List<GetChatRoomRes> getChatRoomResList;
    try {
      getChatRoomResList = chatDao.getChatRoomOne(chatRoomIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
    if (getChatRoomResList.size() != 1) {
      throw new BaseException(DATABASE_ERROR);
    }
    return getChatRoomResList.get(0);
  }

  public int joinRoom(int chatRoomIdx, int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    try {
      return chatDao.createChatMember(chatRoomIdx, userIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public int exitRoom(int chatRoomIdx, int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    try {
      return chatDao.deleteChatMember(chatRoomIdx, userIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public List<GetChatMessageRes> getChatMessage(int chatRoomIdx, int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    try {
      List<GetChatMessageRes> getChatMessageResList = chatDao.getChatMessages(chatRoomIdx);
      if (!getChatMessageResList.isEmpty()) {
        Collections.reverse(getChatMessageResList);
      }
      return getChatMessageResList;
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public GetChatMessageRes createChatMessage(int chatRoomIdx, int userIdx, String chatMessage) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    try {
      int chatMessageIdx = chatDao.createChatMessage(chatRoomIdx, userIdx, chatMessage);
      return chatDao.getChatMessage(chatMessageIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public List<GetChatMessageRes> getMoreChatMessage(int chatRoomIdx, int userIdx, int chatMessageIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    try {
      List<GetChatMessageRes> getChatMessageResList = chatDao.getChatMessageByIdx(chatRoomIdx, chatMessageIdx);
      if (!getChatMessageResList.isEmpty()) {
        Collections.reverse(getChatMessageResList);
      }
      return getChatMessageResList;
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public List<GetChatMemberRes> getChatMembers(int chatRoomIdx, int userIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (chatDao.checkChatMember(chatRoomIdx, userIdx) != 1) {
      throw new BaseException(INVALID_USER_JWT);
    }
    try {
      return chatDao.getChatMember(chatRoomIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }

  public GetChatMatchRoomRes getChatMatchRoom(int userIdx, int matchedUserIdx) throws BaseException {
    if (userDao.checkUser(userIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    if (userDao.checkUser(matchedUserIdx) != 1) {
      throw new BaseException(INVALID_USERIDX);
    }
    // 둘이 서로 매칭된건지 확인하는 로직 필요
    try {
      return chatDao.getChatMatchRoom(userIdx, matchedUserIdx);
    }
    catch (Exception exception) {
      throw new BaseException(DATABASE_ERROR);
    }
  }
}
