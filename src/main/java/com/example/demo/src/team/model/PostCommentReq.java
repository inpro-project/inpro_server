package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@ApiModel(value = "댓글 생성 API Request")
public class PostCommentReq {
    @ApiModelProperty(value = "댓글을 달 팀 인덱스", example = "1")
    private int teamIdx;

    @ApiModelProperty(value = "상위 댓글 인덱스(commentIdx)", example = "1")
    private int parentIdx;

    @ApiModelProperty(value = "댓글 내용", example = "안녕하세요")
    private String content;
}
