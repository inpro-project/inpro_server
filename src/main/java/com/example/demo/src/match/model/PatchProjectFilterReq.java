package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 매칭 필터링 수정 API Request")
public class PatchProjectFilterReq {
    private List<String> typeInsert;
    private List<Integer> typeDelete;

    private List<String> regionInsert;
    private List<Integer> regionDelete;

    private List<String> interestsInsert;
    private List<Integer> interestsDelete;
}
