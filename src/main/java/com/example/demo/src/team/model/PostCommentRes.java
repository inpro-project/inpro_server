package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@ApiModel(value = "댓글 생성 API Response")
public class PostCommentRes {
    @ApiModelProperty(value = "댓글 인덱스", example = "1")
    private int commentIdx;
}
