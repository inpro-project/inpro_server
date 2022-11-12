package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀원 수락 API Response")
public class PostMemberRes {
    @ApiModelProperty(value = "팀원 인덱스", example = "1")
    private int teamMemberIdx;
}
