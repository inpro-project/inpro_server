package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "채팅방 메시지 조회 API response")
public class GetChatMessageRes {
  @ApiModelProperty(value = "채팅 메시지 인덱스", example = "1")
  private int chatMessageIdx;

  @ApiModelProperty(value = "멤버 유저 인덱스", example = "1")
  private int userIdx;

  @ApiModelProperty(value = "채팅 메시지", example = "hello world!")
  private String chatMessage;

  @ApiModelProperty(value = "생성 날짜", example = "2022")
  private String createdAt;
}
