package com.example.demo.src.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "채팅방 멤버 조회 API response")
public class GetChatMemberRes {
  @ApiModelProperty(value = "채팅 멤버 인덱스", example = "1")
  private int chatMemberIdx;

  @ApiModelProperty(value = "멤버 유저 인덱스", example = "1")
  private int userIdx;

  @ApiModelProperty(value = "멤버 닉네임", example = "member1")
  private String nickName;

  @ApiModelProperty(value = "멤버 프로필 이미지 주소", example = "-")
  private String userImageUrl;
}
