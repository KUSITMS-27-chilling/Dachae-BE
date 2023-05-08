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
        SELECT_REGION_SUCCESS("관심 지역 조회에 성공했습니다."),
        EDIT_REGION_SUCCESS("관심 지역 수정에 성공했습니다.");
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum UserFailMessage {
        NOT_FOUND_USER("아이디 혹은 비밀번호가 틀렸습니다."),
        AUTHORIZATION_FAIL("회원 권한이 없습니다.");
        private final String message;
    }

    @Getter
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
}
