package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀 매칭 필터링 등록 API Request")
public class PostProjectFilterReq {
    private List<String> type;
    private List<String> region;
    private List<String> interests;
}
