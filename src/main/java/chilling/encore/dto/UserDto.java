package chilling.encore.dto;

import chilling.encore.global.config.jwt.TokenInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class UserDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "회원가입을 위한 요청객체")
    public static class UserSignUpRequest {
        private String id;
        private String name;
        private String gender;
        private int age;
        private String email;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;
        private String nickName;
        private String phoneNumber;
        private String region;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "소셜 회원가입을 위한 요청객체")
    public static class Oauth2SignUpRequest {
        private String id;
        private String name;
        private String gender;
        private int age;
        private String email;
        private String phoneNumber;
        private String provider;
        private String region;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(description = "로그인을 위한 요청객체")
    public static class UserLoginRequest {
        @ApiModelProperty(notes = "아이디를 입력해주세요")
        private String id;

        @ApiModelProperty(notes = "비밀번호를 입력해주세요")
        private String password;
    }

    @Getter
    @Builder
    @ApiModel(description = "로그인을 위한 응답객체")
    public static class UserLoginResponse {
        private String accessToken;
        private String refreshToken;

        public static UserLoginResponse from(TokenInfoResponse tokenInfoResponse) {
            return UserLoginResponse.builder()
                    .accessToken(tokenInfoResponse.getAccessToken())
                    .refreshToken(tokenInfoResponse.getRefreshToken())
                    .build();
        }
    }
}
