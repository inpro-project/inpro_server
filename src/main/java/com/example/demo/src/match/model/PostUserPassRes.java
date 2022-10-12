package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "유저 넘기기 API response")
public class PostUserPassRes {
    @ApiModelProperty(value = "유저 넘기기 인덱스", example = "1")
    private int userPassIdx;
}
