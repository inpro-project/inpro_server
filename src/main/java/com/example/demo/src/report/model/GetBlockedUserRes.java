package com.example.demo.src.report.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "차단한 유저 조회 API Response")
public class GetBlockedUserRes {

    @ApiModelProperty(value = "차단 인덱스", example = "1")
    private int blockIdx;

    @ApiModelProperty(value = "차단한 유저 인덱스", example = "1")
    private int blockedUserIdx;

    @ApiModelProperty(value = "유저 닉네임", example = "신디")
    private String nickName;

    @ApiModelProperty(value = "유저 이미지 주소", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg")
    private String userImgUrl;

    @ApiModelProperty(value = "성별", example = "여")
    private String gender;

    @ApiModelProperty(value = "연령대", example = "20대")
    private String ageRange;

}
