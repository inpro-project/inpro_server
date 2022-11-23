package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 댓글 전체 조회 API")
public class GetCommentsRes {
    @ApiModelProperty(value = "댓글 인덱스", example = "1")
    private int commentIdx;

    @ApiModelProperty(value = "댓글을 단 유저 인덱스", example = "1")
    private int userIdx;

    @ApiModelProperty(value = "유저 닉네임", example = "신예빈")
    private String nickName;

    @ApiModelProperty(value = "유저 프로필 이미지 주소", example = "url")
    private String userImgUrl;

    @ApiModelProperty(value = "댓글 내용", example = "질문 드립니다.")
    private String content;

    @ApiModelProperty(value = "댓글 생성 시간", example = "22.11.22 오전 10:57")
    private String createdAt;

    @ApiModelProperty(value = "댓글에 달린 대댓글 리스트")
    private List<Reply> replys;
}
