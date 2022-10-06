package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel("포트폴리오 등록 API Response")
public class PostPortfolioRes {
    @ApiModelProperty(value = "포트폴리오 인덱스", example = "1")
    private int portfolioIdx;
}
