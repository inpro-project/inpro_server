package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저 태그 리스트")
public class UserTag {
    @ApiModelProperty(value = "유저 태그 인덱스", example = "1")
    private int userTagIdx;

    @ApiModelProperty(value = "태그 이름", example = "웹개발")
    private String name;
}
