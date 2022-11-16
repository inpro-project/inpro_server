package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 등록 API Request")
public class PostTeamReq {
    @ApiModelProperty(value = "팀 제목", example = "인플 프로젝트")
    private String title;
    @ApiModelProperty(value = "팀 본문글", example = "클라이언트, 서버, 머신러닝 개발자 구합니다.")
    private String content;
    @ApiModelProperty(value = "팀 유형", example = "프로젝트")
    private String type;
    @ApiModelProperty(value = "지역", example = "인천")
    private String region;
    @ApiModelProperty(value = "분야", example = "IT/인터넷")
    private String interests;
}
