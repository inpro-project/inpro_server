package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "유저 업무 성향")
public class GetUserDiscRes {
    @ApiModelProperty(value = "유저 업무 성향 인덱스", example = "1")
    private int userDiscIdx;
    @ApiModelProperty(value = "x좌표", example = "-6.692130429902465")
    private double x;
    @ApiModelProperty(value = "y좌표", example = "-2.828427124746188")
    private double y;
}
