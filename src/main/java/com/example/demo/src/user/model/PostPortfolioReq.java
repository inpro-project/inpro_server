package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "포트폴리오 등록 API Request")
public class PostPortfolioReq {

    @ApiModelProperty(value = "포트폴리오 제목", example = "인플 어플 개발 프로젝트", required = true)
    private String title;

    @ApiModelProperty(value = "포트폴리오 세부 설명", example = "서버 개발 담당 - java, spring-boot 사용")
    private String content;

    @ApiModelProperty(value = "포트폴리오 url", example = "https://github.com/inpro-project/inpro_server.git")
    private String url;

    @ApiModelProperty(value = "대표 여부(하나만 외부 노출)", example = "N", notes = "Y : 대표, N : 대표 아님")
    private String isRepPortfolio;

}
