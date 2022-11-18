package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "나와 매칭된 팀 조회 API response")
public class GetMatchedTeamRes {
    @ApiModelProperty(value = "나와 매칭된 팀 인덱스", example = "1")
    private int matchedTeamIdx;

    @ApiModelProperty(value = "팀 대표 이미지 주소")
    private String teamRepUrl;

    @ApiModelProperty(value = "팀 제목", example = "인플 프로젝트")
    private String title;

    @ApiModelProperty(value = "팀 유형", example = "프로젝트")
    private String type;

    @ApiModelProperty(value = "지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "관심 분야", example = "IT")
    private String interests;
}
