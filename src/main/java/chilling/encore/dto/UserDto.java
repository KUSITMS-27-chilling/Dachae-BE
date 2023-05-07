package chilling.encore.dto;

import chilling.encore.domain.User;
import chilling.encore.global.config.jwt.TokenInfoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
        private String profile;
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
        private String profile;
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

    @Getter
    @Builder
    @ApiModel(description = "회원 등급 모듈을 위한 응답객체")
    public static class UserGrade {
        private String profile;
        private String nickName;
        private int grade;
        private List<String> favFiled;

        public static UserGrade from(User user) {
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));
            return UserGrade.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .grade(user.getGrade())
                    .favFiled(favField)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class UserFavCenter {
        private final List<String> centers;

        public static UserFavCenter from(List<String> centers) {
            return UserFavCenter.builder()
                    .centers(centers)
                    .build();
        }
    }
}
