package com.example.demo.src.like.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저 좋아요 API response")
public class PostUserLikeRes {
    @ApiModelProperty(value = "유저 좋아요 인덱스", example = "1")
    private int userLikeIdx;
}
