package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀원 평가지 조회 API")
public class GetReviewRes {
    @ApiModelProperty(value = "disc feature 인덱스", example = "18")
    private int discFeatureIdx;
    @ApiModelProperty(value = "이름", example = "i")
    private String name;
    @ApiModelProperty(value = "특징", example = "열정적")
    private String feature;
}
