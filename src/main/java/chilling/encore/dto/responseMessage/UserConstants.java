package chilling.encore.dto.responseMessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class UserConstants {

    @Getter
    @RequiredArgsConstructor
    public enum SuccessMessage {
        SIGNUP_SUCCESS("회원가입에 성공했습니다."),
        LOGIN_SUCCESS("로그인에 성공했습니다."),
        SIGNUP_CONTINUE("회원가입 추가진행 기존의 회원이 아닙니다."),
        EMAIL_SEND_SUCCESS("EMAIL 인증번호 전송에 성공했습니다."),
        CHECK_DUP("중복확인 조회에 성공했습니다."),
        SELECT_GRADE_SUCCESS("회원 등급 조회에 성공했습니다."),
        SELECT_LEARNING_INFO_SUCCESS("배움 소식 조회에 성공했습니다."),
        SELECT_INFO_SUCCESS("회원 정보 조회에 성공했습니다."),
        SELECT_REGION_SUCCESS("관심 지역 조회에 성공했습니다."),
        EDIT_NICKNAME_SUCCESS("닉네임 수정에 성공했습니다."),
        EDIT_FAV_FIELD_SUCCESS("활동 분야 수정에 성공했습니다."),
        EDIT_REGION_SUCCESS("관심 지역 수정에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UserFailMessage {
        NOT_FOUND_USER("[ERROR] 아이디 혹은 비밀번호가 틀렸습니다."),
        NO_SUCH_IDX_MESSAGE("[ERROR] 해당 유저가 존재하지 않습니다."),
        AUTHORIZATION_FAIL("[ERROR] 회원 권한이 없습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UserFailCode {
        NOT_FOUND_USER("ua001"),
        NO_SUCH_IDX_CODE("ua002"),
        AUTHORIZATION_FAIL("ua003");
        private final String code;
    }

    @Getter
    public enum Role {
        ROLE_USER, ROLE_ADMIN, ROLE_TEACHER
    }
}
