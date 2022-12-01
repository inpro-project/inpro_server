package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "매칭된 상대와의 채팅방 조회 API")
public class GetChatMatchRoomRes {
  @ApiModelProperty(value = "채팅방 인덱스", example = "1")
  private int chatRoomIdx;
}
