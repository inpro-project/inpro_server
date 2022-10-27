package com.example.demo.src.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "신고하기 API Request")
public class PostReportReq {
    @ApiModelProperty(value = "신고 카테고리")
    private String category;
    @ApiModelProperty(value = "신고 글")
    private String content;
}
