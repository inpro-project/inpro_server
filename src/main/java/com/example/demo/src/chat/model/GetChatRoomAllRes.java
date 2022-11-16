package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "채팅방 목록 조회 API response")
public class GetChatRoomAllRes {
  @ApiModelProperty(value = "채팅방 인덱스", example = "1")
  private int chatRoomIdx;

  @ApiModelProperty(value = "채팅방 메시지 수", example = "100")
  private int chatMessageCount;

  @ApiModelProperty(value = "채팅방 이름", example = "인플팀 회의방")
  private String name;

  @ApiModelProperty(value = "채팅방 한 줄 소개", example = "인플 프로젝트 채팅방")
  private String content;

  @ApiModelProperty(value = "최근 메시지", example = "hello world!")
  private String message;
}
