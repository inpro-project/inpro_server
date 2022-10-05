package com.example.demo.src.disc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class PostUserDiscReq {
    private List<DiscTest> goodList;
    private List<DiscTest> badList;
}
