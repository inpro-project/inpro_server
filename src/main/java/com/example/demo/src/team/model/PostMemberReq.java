package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀원 수락 API Request")
public class PostMemberReq {

    @ApiModelProperty(value = "팀원으로 추가할 유저 인덱스", example = "1")
    @NotNull
    private int userIdx;

    @ApiModelProperty(value = "팀원을 추가할 팀 인덱스", example = "1")
    @NotNull
    private int teamIdx;

    @ApiModelProperty(value = "팀원 역할", example = "팀원")
    private String role;

}
