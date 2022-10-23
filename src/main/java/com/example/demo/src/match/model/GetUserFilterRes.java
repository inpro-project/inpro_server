package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiOperation(value = "팀원 매칭 필터링 조회 API Response")
public class GetUserFilterRes {
    @ApiModelProperty(value = "팀원 매칭 필터링 인덱스", example = "1")
    private int userFilterIdx;
    @ApiModelProperty(value = "카테고리", example = "1 : 연령대, 2 : 지역, 3 : 직업군,  4 : 분야")
    private int category;
    @ApiModelProperty(value = "이름", example = "무관")
    private String name;
}
