package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 이미지 전체 조회 API")
public class GetTeamImgsRes {
    @ApiModelProperty(value = "팀 이미지 인덱스", example = "1")
    private int teamfileIdx;

    @ApiModelProperty(value = "대표 이미지 여부", example = "Y")
    private String isRepImg;

    @ApiModelProperty(value = "팀 이미지 이름", example = "대표 이미지.jpg")
    private String fileName;

    @ApiModelProperty(value = "팀 이미지 주소", example = "url")
    private String teamFileUrl;
}
