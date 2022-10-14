package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String email;
    private String nickName;
    private String userImgUrl;
    private String ageRange;
    private String gender;
    private String status;
}
