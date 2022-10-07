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
    POST_DISC_EMPTY_GOODLIST(false, 2004, "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_DISC_EMPTY_BADLIST(false, 2005, "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_DISC_EMPTY_NAME(false, 2006, "업무 유형 name을 입력해주세요"),
    POST_DISC_INVALID_IDX(false, 2007, "올바르지 않은 discFeatureIdx입니다."),

    // [PATCH] /app/profiles
    PATCH_USER_EMPTY_NICKNAME(false, 2008, "닉네임은 최소 2자, 최대 10자까지 입력 가능합니다."),
    PATCH_USER_INVALID_NICKNAME(false, 2009, "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능합니다."),
    PATCH_USER_EMPTY_REGION(false, 2010, "거주 지역을 입력해주세요."),
    PATCH_USER_EMPTY_OCCUPATION(false, 2011, "직업군을 입력해주세요."),
    PATCH_USER_EMPTY_JOB(false, 2012, "직업을 입력해주세요."),
    PATCH_USER_EMPTY_INTERESTS(false, 2013, "관심 분야를 입력해주세요."),
    PATCH_USER_EMPTY_IMG(false, 2014, "프로필 사진을 입력해주세요."),

    // [POST] /app/portfolios/:portfolioCategoryIdx
    PORTFOLIO_EMPTY_TITLE(false, 2015, "포트폴리오 제목을 입력해주세요."),
    POST_PORTFOLIO_INVALID_IDX(false, 2016, "올바르지 않은 portfolioCategoryIdx 입니다."),

    // [POST] /app/portfolios/:portfolioIdx
    PORTFOLIO_INVALID_PORTFOLIOIDX(false, 2017, "올바르지 않은 portfolioIdx입니다."),

    PORTFOLIO_INVALID_ISREP(false, 2018, "올바르지 않은 대표 여부입니다. (Y나 N만 가능)"),

    // [POST] /app/usertags
    POST_USERTAG_EMPTY_NAME(false, 2019, "태그 이름을 입력해주세요."),
    POST_USERTAG_INVALID_SIZE(false, 2020, "태그는 최대 3개까지 추가 가능합니다."),
    POST_USERTAG_INVALID_LENGTH(false, 2021, "태그 이름은 5글자 이하로 입력이 가능합니다."),

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
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    // [PATCH] /app/profiles
    MODIFY_FAIL_USER(false,4002,"프로필 수정에 실패하였습니다."),

    // [PATCH] /app/portfolios/:portfolioIdx
    MODIFY_FAIL_PORTFOLIO(false, 4003, "포트폴리오 수정에 실패하였습니다."),

    // [DELETE] /app/portfolios/:portfolioIdx
    DELETE_FAIL_PORTFOLIO(false, 4004, "포트폴리오 삭제에 실패하였습니다.");

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
