package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "태그 등록 response body")
public class PostUserTagRes {
    @ApiModelProperty(value = "태그 인덱스", example = "1")
    private int userTagIdx;
}
