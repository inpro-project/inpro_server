package com.example.demo.src.chat.model;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoom {

  private String roomId;
  private String roomName;

  public static ChatRoom create(String name) {
    ChatRoom room = new ChatRoom();
    room.roomId = UUID.randomUUID().toString();
    room.roomName = name;
    return room;
  }
}
