package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "나에게 보낸 좋아요 조회 API response")
public class GetUserLikerRes {
    @ApiModelProperty(value = "나에게 좋아요를 보낸 유저 인덱스", example = "1")
    private int likerIdx;

    @ApiModelProperty(value = "닉네임", example = "신예빈")
    private String nickName;

    @ApiModelProperty(value = "프로필 이미지", example = "https://inpro-buckets.s3.ap-northeast-2.amazonaws.com/profileImg/f1098538-7098-4428-b391-bddda13862df.jpg")
    private String userImgUrl;

    @ApiModelProperty(value = "성별", example = "여")
    private String gender;

    @ApiModelProperty(value = "연령대", example = "20대")
    private String ageRange;

    @ApiModelProperty(value = "거주 지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "직업군", example = "직장인")
    private String occupation;

    @ApiModelProperty(value = "직업", example = "개발자")
    private String job;

    @ApiModelProperty(value = "관심 분야", example = "IT")
    private String interests;
}
