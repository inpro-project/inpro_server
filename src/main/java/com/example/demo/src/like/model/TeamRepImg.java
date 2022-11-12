package com.example.demo.src.like.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 대표 이미지")
public class TeamRepImg {
    @ApiModelProperty(value = "팀 대표 이미지 이름", example = "대표 이미지.jpg")
    private String fileName;
    @ApiModelProperty(value = "팀 대표 이미지 주소", example = "https://inpro-buckets.s3.ap-northeast-2.amazonaws.com/teamImg/2dfb0319-5bfc-48cc-bcda-a39b344f41dd.jpg")
    private String teamFileUrl;
}
