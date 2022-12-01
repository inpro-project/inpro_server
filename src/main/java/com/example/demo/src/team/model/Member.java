package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "멤버 정보 조회")
public class Member {
    @ApiModelProperty(value = "유저 인덱스", example = "1")
    private int userIdx;

    @ApiModelProperty(value = "역할", example = "팀원")
    private String role;

    @ApiModelProperty(value = "유저 닉네임", example = "신예빈")
    private String nickName;

    @ApiModelProperty(value = "연령대", example = "20대")
    private String ageRange;

    @ApiModelProperty(value = "거주 지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "직업군", example = "대학생")
    private String occupation;

    @ApiModelProperty(value = "관심분야", example = "IT/인터넷")
    private String interests;

    @ApiModelProperty(value = "유저 프로필 이미지", example = "url")
    private String userImgUrl;

    @ApiModelProperty(value = "현재 로그인을 한 유저가 리뷰를 남긴 멤버인지 여부", example = "1")
    private int isReview;
}
