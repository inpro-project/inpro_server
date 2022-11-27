package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.ChatMessage;
import com.example.demo.src.chat.model.GetChatMessageRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final ChatService chatService;
  private final JwtService jwtService;

  @MessageMapping("/chat/message")
  public void enter(ChatMessage message) {
    try {
      int userIdx = Integer.parseInt(message.getSender());
      int chatRoomIdx = Integer.parseInt(message.getRoomId());
      String chatMessage = message.getMessage();

      if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
        message.setMessage(message.getSender()+"님이 입장하였습니다.");
      }
      GetChatMessageRes getChatMessageRes = chatService.createChatMessage(chatRoomIdx, userIdx, chatMessage);
      message.setCreatedAt(getChatMessageRes.getCreatedAt());
    }
    catch (BaseException exception) {
      return;
    }
    sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
  }
}
