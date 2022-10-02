package com.example.demo.src.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class KakaoUser {
    private String email;
    private String nickName;
    private String userImgUrl;
    private String age_range;
    private String gender;
}
