package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "팀원 매칭 필터링 수정 API Request")
public class PatchUserFilterReq {
    @ApiModelProperty(value = "추가할 연령대", example = "10대")
    private List<String> ageRangeInsert;
    @ApiModelProperty(value = "삭제할 연령대 인덱스", example = "1")
    private List<Integer> ageRangeDelete;

    private List<String> regionInsert;
    private List<Integer> regionDelete;

    private List<String> occupationInsert;
    private List<Integer> occupationDelete;

    private List<String> interestsInsert;
    private List<Integer> interestsDelete;
}
