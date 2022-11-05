package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "채팅방 생성 API response")
public class PostChatRoomRes {
  @ApiModelProperty(value = "채팅방 인덱스", example = "1")
  private int chatRoomIdx;

  @ApiModelProperty(value = "채팅방 멤버 인덱스", example = "1")
  private int chatMemberIdx;
}
