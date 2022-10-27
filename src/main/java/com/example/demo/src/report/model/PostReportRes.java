package com.example.demo.src.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "신고하기 API Response")
public class PostReportRes {
    @ApiModelProperty(value = "신고 인덱스", example = "1")
    private int reportIdx;
}
