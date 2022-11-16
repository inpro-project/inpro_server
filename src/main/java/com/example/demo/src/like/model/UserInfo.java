package com.example.demo.src.like.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel("유저 프로필 정보 출력")
public class UserInfo {

    @ApiModelProperty(value = "유저 인덱스", example = "1")
    private int userIdx;

    @ApiModelProperty(value = "닉네임", example = "신예빈")
    private String nickName;

    @ApiModelProperty(value = "프로필 이미지 주소", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg")
    private String userImgUrl;

    @ApiModelProperty(value = "성별", example = "여")
    private String gender;

    @ApiModelProperty(value = "연령대", example = "20대")
    private String ageRange;

    @ApiModelProperty(value = "거주 지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "직업군", example = "직장인")
    private String occupation;

    @ApiModelProperty(value = "관심 분야", example = "IT")
    private String interests;
}
