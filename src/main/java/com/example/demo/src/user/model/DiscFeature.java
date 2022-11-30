package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "업무 성향 특징 태그 리스트")
public class DiscFeature {
    @ApiModelProperty(value = "업무 성향 특징 인덱스", example = "1")
    private int discFeatureIdx;
    @ApiModelProperty(value = "업무 성향 특징", example = "근면")
    private String feature;
}
