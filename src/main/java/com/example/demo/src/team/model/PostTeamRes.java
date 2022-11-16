package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 등록 API Response")
public class PostTeamRes {
    @ApiModelProperty(value = "팀 인덱스", example = "1")
    private int teamIdx;
}
