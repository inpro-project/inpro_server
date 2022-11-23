package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 300 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 300, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 301, "JWT를 입력해주세요."),
    INVALID_JWT(false, 302, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,303,"권한이 없는 유저의 접근입니다."),

    // [POST] /app/user-discs
    POST_DISC_EMPTY_GOODLIST(false, 304, "적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_DISC_EMPTY_BADLIST(false, 305, "부적합에 대한 업무 유형 테스트 결과를 입력해주세요."),
    POST_DISC_EMPTY_NAME(false, 306, "업무 유형 name을 입력해주세요"),
    POST_DISC_INVALID_IDX(false, 307, "올바르지 않은 discFeatureIdx입니다."),

    // [PATCH] /app/profiles
    PATCH_USER_EMPTY_NICKNAME(false, 308, "닉네임은 최소 2자, 최대 10자까지 입력 가능합니다."),
    PATCH_USER_INVALID_NICKNAME(false, 309, "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능합니다."),
    PATCH_USER_EMPTY_REGION(false, 310, "거주 지역을 입력해주세요."),
    PATCH_USER_EMPTY_OCCUPATION(false, 311, "직업군을 입력해주세요."),
    PATCH_USER_EMPTY_INTERESTS(false, 313, "관심 분야를 입력해주세요."),
    PATCH_USER_EMPTY_IMG(false, 314, "프로필 사진을 입력해주세요."),

    // [POST] /app/portfolios/:portfolioCategoryIdx
    PORTFOLIO_EMPTY_TITLE(false, 315, "포트폴리오 제목을 입력해주세요."),
    POST_PORTFOLIO_INVALID_IDX(false, 316, "올바르지 않은 portfolioCategoryIdx 입니다."),

    // [POST] /app/portfolios/:portfolioIdx
    PORTFOLIO_INVALID_PORTFOLIOIDX(false, 317, "올바르지 않은 portfolioIdx입니다."),

    PORTFOLIO_INVALID_ISREP(false, 318, "올바르지 않은 대표 여부입니다. (Y나 N만 가능)"),

    // [POST] /app/usertags
    POST_USERTAG_EMPTY_NAME(false, 319, "태그 이름을 입력해주세요."),
    POST_USERTAG_INVALID_SIZE(false, 320, "태그는 최대 3개까지 추가 가능합니다."),
    POST_USERTAG_INVALID_LENGTH(false, 321, "태그 이름은 5글자 이하로 입력이 가능합니다."),

    // [DELETE] /app/usertags/:userTagIdx
    DELETE_USERTAG_INVALID_USERTAGIDX(false, 322, "올바르지 않은 userTagIdx입니다."),

    // [GET] /app/user-discs/:userDiscIdx [PATCH] /app/user-discs/:userDiscIdx
    USERDISC_INVALID_USERDISCIDX(false, 323, "올바르지 않은 userDiscIdx입니다."),

    // [GET] /app/search-discs/:searchDiscIdx [PATCH] /app/search-discs/:searchDiscIdx
    SEARCHDISC_INVALID_SEARCHDISCIDX(false, 324, "올바르지 않은 searchDiscIdx입니다."),

    // [POST] /app/user-likes/:likingIdx
    USERLIKE_INVALID_LIKINGIDX(false, 325, "이미 좋아요를 누른 유저입니다."),

    INVALID_USERIDX(false, 326, "유효하지 않은 유저 인덱스입니다."),

    // [PATCH] /app/user-likes/:likingIdx
    UNUSERLIKE_INVALID_LIKINGIDX(false, 327, "기존에 좋아요를 누르지 않은 유저입니다."),

    // [POST] /app/user-passes/:passingIdx
    USERPASS_INVALID_PASSINGIDX(false, 328, "이미 넘기기를 누른 유저입니다."),

    // [POST] /app/blocks/:blockedUserIdx
    BLOCK_INVALID_BLOCKEDUSERIDX(false, 329, "이미 차단을 누른 유저입니다."),

    // [PATCH] /app/blocks/:blockedUserIdx
    UNBLOCK_INVALID_BLOCKEDUSERIDX(false, 330, "기존에 차단을 하지 않은 유저입니다."),

    // [POST] /app/reports/:reportedUserIdx
    POST_REPORT_EMPTY_CATEGORY(false, 331, "신고 카테고리를 입력해주세요."),

    // [POST] /app/reports/:reportedUserIdx, [POST] /app/teams
    POST_FILE_MAX(false, 332, "일반 파일은 5개 이하 첨부 가능합니다."),
    POST_IMG_MAX(false, 333, "사진 파일은 5개 이하 첨부 가능합니다."),
    POST_FILE_EXT(false, 334, "doc(docx), hwp, pdf, xls(xlsx) 확장자의 일반 파일만 업로드 가능합니다."),
    POST_IMG_EXT(false, 335, "jpeg, jpg, png, gif, bmp 확장자의 사진 파일만 업로드 가능합니다."),

    // [PATCH] /app/user-passes/:passingIdx
    UNUSERPASS_INVALID_PASSINGIDX(false, 336, "기존에 넘기기를 누르지 않은 유저입니다."),

    // [POST] /app/teams
    POST_TEAM_EMPTY_TITLE(false, 337, "제목을 입력해주세요."),
    POST_TEAM_MAXSIZE_TITLE(false, 338, "제목은 20글자까지 입력이 가능합니다."),
    POST_TEAM_EMPTY_CONTENT(false, 339, "내용을 입력해주세요."),
    POST_TEAM_EMPTY_TYPE(false, 340, "팀 유형을 입력해주세요."),
    POST_TEAM_EMPTY_REGION(false, 341, "지역을 입력해주세요"),
    POST_TEAM_EMPTY_INTERESTS(false, 342, "분야를 입력해주세요."),

    // [POST] /app/team-likes/:likingIdx
    TEAMLIKE_INVALID_LIKINGIDX(false, 343, "이미 좋아요를 누른 팀입니다."),

    INVALID_TEAMIDX(false, 344, "유효하지 않은 팀 인덱스입니다."),

    // [PATCH] /app/team-likes/:likingIdx
    UNTEAMLIKE_INVALID_LIKINGIDX(false, 345, "기존에 좋아요를 누르지 않은 팀입니다."),

    // [POST] /app/team-passes/:passingIdx
    TEAMPASS_INVALID_PASSINGIDX(false, 346, "이미 넘기기를 누른 팀입니다."),

    // [PATCH] /app/team-passes/:passingIdx
    UNTEAMPASS_INVALID_PASSINGIDX(false, 347, "기존에 넘기기를 누르지 않은 팀입니다."),

    // [POST] /app/members
    MEMBER_INVALID_USERIDX(false, 348, "이미 멤버로 추가된 유저입니다."),

    // [POST] /app/comments
    POST_COMMENT_EMPTY_CONTENT(false, 349, "댓글 내용을 입력해주세요."),
    POST_COMMENT_EMPTY_TEAMIDX(false, 350, "팀 인덱스를 입력해주세요."),
    INVALID_COMMENTIDX(false, 351, "유효하지 않은 댓글 인덱스입니다."),

    // [DELETE] /app/comments/:commentIdx
    DELETE_COMMENT_INVALID_COMMENTIDX(false, 352, "올바르지 않은 댓글 인덱스입니다."),

    /**
     * 400 : Response, Database, Server 오류
     */
    DATABASE_ERROR(false, 400, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 401, "서버와의 연결에 실패하였습니다."),

    RESPONSE_ERROR(false, 402, "값을 불러오는데 실패하였습니다."),

    // [GET] /app/oauth/kakao
    INACTIVE_USER(false, 403, "비활성화된 유저입니다."),
    DELETED_USER(false, 404, "탈퇴한 유저입니다."),

    // [PATCH] /app/profiles
    MODIFY_FAIL_USER(false,405,"프로필 수정에 실패하였습니다."),

    // [PATCH] /app/portfolios/:portfolioIdx
    MODIFY_FAIL_PORTFOLIO(false, 406, "포트폴리오 수정에 실패하였습니다."),

    // [DELETE] /app/portfolios/:portfolioIdx
    DELETE_FAIL_PORTFOLIO(false, 407, "포트폴리오 삭제에 실패하였습니다."),

    // [DELETE] /app/usertags/:userTagIdx
    DELETE_FAIL_USERTAG(false, 408, "태그 삭제에 실패하였습니다."),

    // [POST] /app/user-likes/:likingIdx
    FAIL_USERLIKE(false, 409, "유저 좋아요에 실패하였습니다."),

    // [PATCH] /app/user-likes/:likingIdx
    FAIL_UNUSERLIKE(false, 410, "유저 좋아요 취소에 실패하였습니다."),

    // [POST] /app/user-passes/:passingIdx
    FAIL_USERPASS(false, 411, "유저 넘기기에 실패하였습니다."),

    // [PATCH] /app/user-discs/:userDiscIdx
    FAIL_USERDISCNAME(false, 412, "user disc 이름 등록 및 수정에 실패하였습니다."),

    // [PATCH] /app/search-discs/:searchDiscIdx
    FAIL_SEARCHDISCNAME(false, 413, "search disc 이름 등록 및 수정에 실패하였습니다."),

    // [POST] /app/blocks/:blockedUserIdx
    FAIL_BLOCK(false, 414, "유저 차단에 실패하였습니다."),

    // [PATCH] /app/blocks/:blockedUserIdx
    FAIL_UNBLOCK(false, 415, "유저 차단 해제에 실패하였습니다."),

    // [POST] /app/reports/:reportedUserIdx, [POST] /app/teams
    FAIL_IMG(false, 416, "사진 파일 첨부에 실패하였습니다."),
    FAIL_FILE(false, 417, "일반 파일 첨부에 실패하였습니다."),

    // [PATCH] /app/user-passes/:passingIdx
    FAIL_UNUSERPASS(false, 418, "유저 넘기기 취소에 실패하였습니다."),

    // [POST] /app/teams
    FAIL_MEMBER(false, 419, "멤버 등록에 실패하였습니다."),

    // [POST] /app/team-likes/:likingIdx
    FAIL_TEAMLIKE(false, 420, "팀 좋아요에 실패하였습니다."),

    // [PATCH] /app/team-likes/:likingIdx
    FAIL_UNTEAMLIKE(false, 421, "팀 좋아요 취소에 실패하였습니다."),

    // [POST] /app/team-passes/:passingIdx
    FAIL_TEAMPASS(false, 422, "팀 넘기기에 실패하였습니다."),

    // [PATCH] /app/team-passes/:passingIdx
    FAIL_UNTEAMPASS(false, 423, "팀 넘기기 취소에 실패하였습니다."),

    // [DELETE] /app/comments/:commentIdx
    DELETE_FAIL_COMMENT(false, 424, "댓글 삭제에 실패하였습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
