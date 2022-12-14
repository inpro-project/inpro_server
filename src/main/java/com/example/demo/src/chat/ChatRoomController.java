package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.GetChatMatchRoomRes;
import com.example.demo.src.chat.model.GetChatMemberRes;
import com.example.demo.src.chat.model.GetChatMessageRes;
import com.example.demo.src.chat.model.GetChatRoomAllRes;
import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.chat.model.PostChatRoomReq;
import com.example.demo.src.chat.model.PostChatRoomRes;
import com.example.demo.utils.JwtService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
  private final ChatService chatService;
  private final JwtService jwtService;

  // 채팅 리스트 화면
  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }

  // 모든 채팅방 목록 반환
  @GetMapping("/rooms")
  @ResponseBody
  public BaseResponse<List<GetChatRoomAllRes>> getChatRoom() {
    try {
      int userIdx = jwtService.getUserIdx();
      List<GetChatRoomAllRes> getChatRoomAllResList = chatService.findAllRoom(userIdx);
      return new BaseResponse<>(getChatRoomAllResList);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  // 채팅방 생성
  @PostMapping("/room")
  @ResponseBody
  public BaseResponse<PostChatRoomRes> createRoom(@RequestParam String name, @RequestParam String content) {
    try{
      int userIdx = jwtService.getUserIdx();
      int chatRoomIdx = chatService.createRoom(userIdx, name, content);
      int chatMemberIdx = chatService.joinRoom(chatRoomIdx, userIdx);
      PostChatRoomRes postChatRoomRes = new PostChatRoomRes(chatRoomIdx, chatMemberIdx);
      return new BaseResponse<>(postChatRoomRes);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @ApiOperation(value = "채팅방 생성 및 채팅방 멤버")
  @PostMapping("/room/match")
  @ResponseBody
  public BaseResponse<PostChatRoomRes> createRoom(@RequestBody PostChatRoomReq postChatRoomReq) {
    try{
      int userIdx = jwtService.getUserIdx();
      int chatRoomIdx = chatService.createRoom(userIdx, postChatRoomReq.getName(), postChatRoomReq.getContent());
      int chatMemberIdx = chatService.joinRoom(chatRoomIdx, userIdx);
      chatService.joinRoom(chatRoomIdx, postChatRoomReq.getMatchedUserIdx());
      PostChatRoomRes postChatRoomRes = new PostChatRoomRes(chatRoomIdx, chatMemberIdx);
      return new BaseResponse<>(postChatRoomRes);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @ApiOperation(value = "채팅방 참여")
  @PostMapping("/room/join")
  @ResponseBody
  public BaseResponse<PostChatRoomRes> joinRoom(@RequestParam int chatRoomIdx) {
    try{
      int userIdx = jwtService.getUserIdx();
      int chatMemberIdx = chatService.joinRoom(chatRoomIdx, userIdx);
      PostChatRoomRes postChatRoomRes = new PostChatRoomRes(chatRoomIdx, chatMemberIdx);
      return new BaseResponse<>(postChatRoomRes);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @ApiOperation(value = "채팅방 나가기")
  @PatchMapping("/room/join")
  @ResponseBody
  public BaseResponse<PostChatRoomRes> exitRoom(@RequestParam int chatRoomIdx) {
    try{
      int userIdx = jwtService.getUserIdx();
      int chatMemberIdx = chatService.exitRoom(chatRoomIdx, userIdx);
      PostChatRoomRes postChatRoomRes = new PostChatRoomRes(chatRoomIdx, chatMemberIdx);
      return new BaseResponse<>(postChatRoomRes);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @ApiOperation(value = "")
  @GetMapping("/room/enter/{chatRoomId}/{chatMessageIdx}")
  @ResponseBody
  public BaseResponse<List<GetChatMessageRes>> getChatRoom(@PathVariable int chatRoomId, @PathVariable int chatMessageIdx) {
    try {
      List<GetChatMessageRes> getChatMessageResList;
      int userIdx = jwtService.getUserIdx();

      if (chatMessageIdx == 0) {
        getChatMessageResList = chatService.getChatMessage(chatRoomId, userIdx);
      }
      else {
        getChatMessageResList = chatService.getMoreChatMessage(chatRoomId, userIdx, chatMessageIdx);
      }
      return new BaseResponse<>(getChatMessageResList);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }


  // 채팅방 입장 화면
  @GetMapping("/room/enter/{roomId}")
  public String roomDetail(Model model, @PathVariable String roomId) {
    model.addAttribute("roomId", roomId);
    return "/chat/roomdetail";
  }

  @GetMapping("/room/{roomId}")
  @ResponseBody
  public BaseResponse<GetChatRoomRes> roomInfo(@PathVariable int roomId) {
    try {
      int userIdx = jwtService.getUserIdx();
      GetChatRoomRes getChatRoomRes = chatService.getRoomById(roomId, userIdx);
      return new BaseResponse<>(getChatRoomRes);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @GetMapping("/room/{roomId}/member")
  @ResponseBody
  public BaseResponse<List<GetChatMemberRes>> getChatMembers(@PathVariable int roomId) {
    try {
      int userIdx = jwtService.getUserIdx();
      List<GetChatMemberRes> getChatMemberResList = chatService.getChatMembers(roomId, userIdx);
      return new BaseResponse<>(getChatMemberResList);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }

  @GetMapping("/room/match/{matchedUserIdx}")
  @ResponseBody
  public BaseResponse<GetChatMatchRoomRes> getChatMatchRoom(@PathVariable int matchedUserIdx) {
    try {
      int userIdx = jwtService.getUserIdx();
      GetChatMatchRoomRes getChatMatchRoomResList = chatService.getChatMatchRoom(userIdx, matchedUserIdx);
      return new BaseResponse<>(getChatMatchRoomResList);
    }
    catch (BaseException exception) {
      return new BaseResponse<>(exception.getStatus());
    }
  }
}
