package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저 프로필 수정 API request body")
public class PatchUserReq {

    @ApiModelProperty(value = "유저 닉네임", example = "이민혁", required = true)
    private String nickName;

    @ApiModelProperty(value = "소개글", example = "개발 분야에 관심이 많습니다.")
    private String comment;

    @ApiModelProperty(value = "거주 지역", example = "인천", required = true)
    private String region;

    @ApiModelProperty(value = "직업군", example = "직장인", required = true)
    private String occupation;

    @ApiModelProperty(value = "관심 분야", example = "IT", required = true)
    private String interests;

}
