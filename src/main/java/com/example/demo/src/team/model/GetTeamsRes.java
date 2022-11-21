package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저가 등록한 팀 전체 조회 API")
public class GetTeamsRes {
    @ApiModelProperty(value = "팀 인덱스", example = "1")
    private int teamIdx;

    @ApiModelProperty(value = "팀 제목", example = "인플 프로젝트")
    private String title;

    @ApiModelProperty(value = "팀 유형", example = "프로젝트")
    private String type;

    @ApiModelProperty(value = "지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "분야", example = "IT/인터넷")
    private String interests;

    @ApiModelProperty(value = "모집 여부", example = "active", notes = "active : 모집 중, inactive : 모집 완료")
    private String status;
}
