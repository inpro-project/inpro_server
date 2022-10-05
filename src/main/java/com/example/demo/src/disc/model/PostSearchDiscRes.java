package com.example.demo.src.disc.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "유저가 찾는 팀원 업무성향 테스트 API response")
public class PostSearchDiscRes {

    @ApiModelProperty(value = "유저가 찾는 팀원 업무성향 인덱스", example = "1")
    private int searchDiscIdx;

}