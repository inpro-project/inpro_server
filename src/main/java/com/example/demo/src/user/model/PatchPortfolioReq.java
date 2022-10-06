package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "포트폴리오 수정 API")
public class PatchPortfolioReq {

    @ApiModelProperty(value = "포트폴리오 제목", example = "인플 어플 개발 프로젝트", required = true)
    private String title;

    @ApiModelProperty(value = "포트폴리오 세부 설명", example = "서버 개발 담당 - java, spring-boot 사용")
    private String content;

    @ApiModelProperty(value = "포트폴리오 url", example = "https://github.com/inpro-project/inpro_server.git")
    private String url;

}
