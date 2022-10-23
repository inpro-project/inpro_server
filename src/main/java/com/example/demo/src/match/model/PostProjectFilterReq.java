package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiOperation(value = "팀 매칭 필터링 조회 API Response")
public class PostProjectFilterReq {

    @ApiModelProperty(value = "유형")
    private List<String> type;

    @ApiModelProperty(value = "지역")
    private List<String> region;

    @ApiModelProperty(value = "분야")
    private List<String> interests;

}
