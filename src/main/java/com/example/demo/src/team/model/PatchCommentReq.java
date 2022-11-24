package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@ApiModel(value = "댓글 수정 API Request")
public class PatchCommentReq {
    @ApiModelProperty(value = "댓글 내용", example = "안녕하세요")
    private String content;
}
