package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

  public void setMessage(String message) {
    this.message = message;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public enum MessageType {
    ENTER, TALK
  }

  private MessageType type;
  //채팅방 ID
  private String roomId;
  //보내는 사람
  private String sender;
  //내용
  private String message;
  //시간
  private String createdAt;
}
