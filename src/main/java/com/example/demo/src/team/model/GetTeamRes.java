package com.example.demo.src.team.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "특정 팀 상세 조회 API")
public class GetTeamRes {
    @ApiModelProperty(value = "리더 인덱스", example = "1")
    private int leaderIdx;

    @ApiModelProperty(value = "팀 채팅방 인덱스", example = "1")
    private int chatRoomIdx;

    @ApiModelProperty(value = "리더 search disc x, y 좌표와 percent")
    private SearchDiscAndPercent searchDiscAndPercent;

    @ApiModelProperty(value = "팀 유형", example = "프로젝트")
    private String type;

    @ApiModelProperty(value = "지역", example = "인천")
    private String region;

    @ApiModelProperty(value = "분야", example = "IT/인터넷")
    private String interests;

    @ApiModelProperty(value = "팀 제목", example = "인플 프로젝트")
    private String title;

    @ApiModelProperty(value = "팀 소개 글", example = "클라이언트, 서버, 머신러닝 개발자 구합니다.")
    private String content;

    @ApiModelProperty(value = "모집 여부", example = "active", notes = "active : 모집 중, inactive : 모집 완료, finish : 프로젝트 종료")
    private String status;

    @ApiModelProperty(value = "팀 대표 이미지", example = "url")
    private String teamImgUrl;

    @ApiModelProperty(value = "팀 좋아요 개수", example = "12")
    private int likeCount;

    @ApiModelProperty(value = "팀 댓글 개수", example = "10")
    private int commentCount;

    @ApiModelProperty(value = "팀에 속한 멤버 수", example = "5")
    private int memberCount;

    @ApiModelProperty(value = "팀에 속한 멤버 리스트")
    private List<Member> members;

    @ApiModelProperty(value = "현재 로그인한 유저의 user disc x좌표", example = "-6.692130429902465")
    private double userDiscX;

    @ApiModelProperty(value = "현재 로그인한 유저의 user disc y좌표", example = "-6.692130429902465")
    private double userDiscY;

}
