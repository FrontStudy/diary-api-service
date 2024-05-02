package com.yuk.wazzangstudyrestapi1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    EMPTY_LOGIN_CREDENTIALS(BAD_REQUEST, "로그인 아이디와 패스워드를 입력해주세요"),
    EMPTY_TITLE_OR_CONTENT(BAD_REQUEST, "게시글의 제목 또는 내용이 비어있습니다"),
    EMPTY_DIARYSHARE_MEMBER(BAD_REQUEST, "공유할 멤버를 입력해주세요"),
    EMPTY_REQUEST(BAD_REQUEST, "요청 파라미터가 비었습니다."),
    SHARE_DIARY_MYSELF(BAD_REQUEST, "자신에게 일기를 공유할 수 없습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    /* 403 FORBIDDEN : 권한 부족 */
    INSUFFICIENT_PERMISSION(FORBIDDEN, "해당 작업을 수행할 권한이 부족합니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    DIARY_NOT_FOUND(NOT_FOUND, "일기 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    CONFLICT_LIKES_STATE(CONFLICT, "좋아요의 현재 상태와의 충돌이 있습니다"),
    CONFLICT_BOOKMARK_STATE(CONFLICT, "북마크의 현재 상태와의 충돌이 있습니다"),
    CONFLICT_FOLLOW_STATE(CONFLICT, "팔로우의 현재 상태와의 충돌이 있습니다"),

    /* 500 Internal Server Error */
    PERSISTENCE_ERROR(INTERNAL_SERVER_ERROR, "데이터베이스 작업 중 오류가 발생했습니다."),
    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}