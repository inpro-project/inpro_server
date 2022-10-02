package com.example.demo.src.oauth.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "카카오 로그인 API response")
public class OAuthLoginRes {
    @ApiModelProperty(value = "유저 식별자", example = "1")
    private int userIdx;

    @ApiModelProperty(value = "유저 jwt 토큰", example = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4IjoxLCJpYXQiOjE2NjQ3MDYxNzgsImV4cCI6MTY2NjE3NzQwN30.-1ODQuXZFKV3PrZ3zF2jBeW9KwSBuEi4id4RVjzCwp4")
    private String jwt;
}
