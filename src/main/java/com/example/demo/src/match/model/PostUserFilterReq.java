package com.example.demo.src.match.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@ApiModel(value = "팀원 매칭 필터링 API Request")
public class PostUserFilterReq {
    private List<String> ageRange;
    private List<String> region;
    private List<String> occupation;
    private List<String> interests;
}
