package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // [POST] /app/user-discs
    POST_USERDISC_EMPTY_GOODLIST(false, 2004, "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_USERDISC_EMPTY_BADLIST(false, 2005, "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_USERDISC_EMPTY_NAME(false, 2006, "업무 유형 name을 입력해주세요"),
    POST_USERDISC_INVALID_IDX(false, 2007, "올바르지 않은 discFeatureIdx입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [GET] /app/oauth/kakao
    INACTIVE_USER(false, 3002, "비활성화된 유저입니다."),
    DELETED_USER(false, 3003, "탈퇴한 유저입니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");

    // 5000
    // 6000


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
