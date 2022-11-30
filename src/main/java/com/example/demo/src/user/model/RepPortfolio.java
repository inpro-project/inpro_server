package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "대표 포트폴리오")
public class RepPortfolio {
    @ApiModelProperty(value = "포트폴리오 카테고리 인덱스", example = "1")
    private int portfolioCategoryIdx;
    @ApiModelProperty(value = "포트폴리오 제목", example = "인플 어플 개발 프로젝트")
    private String title;
}
