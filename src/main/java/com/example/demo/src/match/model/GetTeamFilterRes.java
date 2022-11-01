package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 매칭 필터링 조회 API Response")
public class GetTeamFilterRes {
    @ApiModelProperty(value = "팀 매칭 필터링 인덱스", example = "1")
    private int teamFilterIdx;
    @ApiModelProperty(value = "카테고리", example = "1 : 유형, 2 : 지역, 3 : 분야")
    private int category;
    @ApiModelProperty(value = "이름", example = "무관")
    private String name;
}
