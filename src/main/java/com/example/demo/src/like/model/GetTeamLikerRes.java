package com.example.demo.src.like.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "나에게 보낸 TeamLike 조회 API response")
public class GetTeamLikerRes {

    @ApiModelProperty(value = "팀 인덱스", example = "1")
    private int teamIdx;

    @ApiModelProperty(value = "팀 제목", example = "인플 프로젝트")
    private String title;

    @ApiModelProperty(value = "팀 유형", example = "프로젝트")
    private String type;

    @ApiModelProperty(value = "좋아요를 누른 유저 리스트")
    private List<UserInfo> likers;

}
