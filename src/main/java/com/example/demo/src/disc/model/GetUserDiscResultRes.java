package com.example.demo.src.disc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "User Disc 결과 조회 API response")
public class GetUserDiscResultRes {
    @ApiModelProperty(value = "user disc 인덱스", example = "1")
    private int userDiscIdx;

    @ApiModelProperty(value = "이름", example = "user disc(1)")
    private String name;

    @ApiModelProperty(value = "x 좌표", example = "2.3107890345411475")
    private double x;

    @ApiModelProperty(value = "y 좌표", example = "-2.689726416504161")
    private double y;

    @ApiModelProperty(value = "대표 여부", example = "Y")
    private String isRepDisc;

    @ApiModelProperty(value = "d유형 비중(퍼센트)", example = "18")
    private int dPercent;

    @ApiModelProperty(value = "i유형 비중(퍼센트)", example = "24")
    private int iPercent;

    @ApiModelProperty(value = "s유형 비중(퍼센트)", example = "33")
    private int sPercent;

    @ApiModelProperty(value = "c유형 비중(퍼센트)", example = "25")
    private int cPercent;
}
