package com.example.demo.src.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저 차단 API Response")
public class PostBlockRes {
    @ApiModelProperty(value = "차단 인덱스", example = "1")
    private int blockIdx;
}
