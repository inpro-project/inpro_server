package com.example.demo.src.disc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "업무 성향 테스트지 조회 API response")
public class GetDiscTestRes {
    @ApiModelProperty(value = "업무 성향 인덱스", example = "1")
    private int discIdx;

    @ApiModelProperty(value = "업무 성향", example = "di")
    private String name;

    @ApiModelProperty(value = "업무 성향별 특성 인덱스", example = "1")
    private int discFeatureIdx;

    @ApiModelProperty(value = "업무 성향별 특성", example = "도전적인")
    private String feature;
}
