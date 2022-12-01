package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "매칭된 채팅방 등록 API Request")
public class PostChatRoomReq {
  @ApiModelProperty(value = "채팅방 제목", example = "채팅방 이름", required = true)
  private String name;

  @ApiModelProperty(value = "채팅방 내용", example = "채팅방 내용", required = true)
  private String content;

  @ApiModelProperty(value = "매칭된 상대 userIdx", example = "1", required = true)
  private int matchedUserIdx;
}
