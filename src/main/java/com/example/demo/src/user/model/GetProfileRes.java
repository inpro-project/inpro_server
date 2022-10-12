package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "유저 프로필 상세 조회 API")
public class GetProfileRes {

    @ApiModelProperty(value = "닉네임", example = "신예빈")
    private String nickName;

    @ApiModelProperty(value = "프로필 이미지 주소", example = "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg")
    private String userImgUrl;

    @ApiModelProperty(value = "성별", example = "여")
    private String gender;

    @ApiModelProperty(value = "연령대", example = "20대")
    private String ageRange;

    @ApiModelProperty(value = "한줄 소개", example = "서버 개발에 관심이 많습니다!")
    private String comment;

    @ApiModelProperty(value = "거주 지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "직업군", example = "직장인")
    private String occupation;

    @ApiModelProperty(value = "직업", example = "개발자")
    private String job;

    @ApiModelProperty(value = "관심 분야", example = "IT")
    private String interests;

    @ApiModelProperty(value = "유저 대표 업무 성향")
    private List<GetUserDiscRes> userDisc;

    @ApiModelProperty(value = "유저가 찾는 대표 업무 성향")
    private List<GetSearchDiscRes> searchDisc;

    @ApiModelProperty(value = "업무 성향 특징 태그 리스트")
    private List<GetDiscFeatureRes> discFeatures;

    @ApiModelProperty(value = "유저 태그 리스트")
    private List<GetUserTagRes> userTags;

    @ApiModelProperty(value = "대표 포트폴리오")
    private List<GetRepPortfolioRes> repPortfolio;
}
