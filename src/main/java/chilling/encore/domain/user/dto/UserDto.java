package chilling.encore.domain.user.dto;

import chilling.encore.domain.user.entity.User;
import chilling.encore.global.config.security.jwt.TokenInfoResponse;
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
    @RequiredArgsConstructor
    @ApiModel(description = "로그인을 위한 응답객체")
    public static class UserLoginResponse {
        private final String accessToken;
        private final String refreshToken;

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
        private List<String> favField;

        public static UserGrade from(User user) {
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));
            return UserGrade.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .grade(user.getGrade())
                    .favField(favField)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class UserFavRegion {
        private final List<String> regions;

        public static UserFavRegion from(List<String> regions) {
            return UserFavRegion.builder()
                    .regions(regions)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserRegion {
        private String region;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EditFavRegion {
        private String favRegion;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EditNickName {
        private String nickName;
    }
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EditFavField {
        private String favField;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetTotalWrite {
        private int allTotal;
        private int listenTotal;
        private int reviewTotal;
        private int lectureTotal;
        private int freeTotal;

        public static GetTotalWrite from(int allTotal, int listenTotal, int reviewTotal, int lectureTotal, int freeTotal) {
            return GetTotalWrite.builder()
                    .allTotal(allTotal)
                    .listenTotal(listenTotal)
                    .reviewTotal(reviewTotal)
                    .lectureTotal(lectureTotal)
                    .freeTotal(freeTotal)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetTotalParticipants {
        private int participantTotal;
        private int applyLectureTotal;

        public static GetTotalParticipants from(int participantTotal, int applyLectureTotal) {
            return GetTotalParticipants.builder()
                    .participantTotal(participantTotal)
                    .applyLectureTotal(applyLectureTotal)
                    .build();
        }
    }


    @Getter
    @Builder
    @ApiModel(description = "마이페이지에서 회원 정보 응답객체")
    public static class UserInfo {
        private String profile;
        private String nickName;
        private int grade;
        private List<String> favFiled;
        private List<String> favRegion;

        public static UserInfo from(User user) {
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));
            List<String> favRegion = new ArrayList<>();
            favRegion.add(user.getRegion());
            if (user.getFavRegion() != null) {
                String[] split = user.getFavRegion().split(",");
                for (int i = 0; i < split.length; i++) {
                    favRegion.add(split[i]);
                }
            }
            return UserInfo.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .grade(user.getGrade())
                    .favFiled(favField)
                    .favRegion(favRegion)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class LearningInfo {
        private String learningInfoIdx;
        private String isFin;
        private String listenIdx;
        private String title;
        private String nickName;
        public static LearningInfo from(String learningInfoIdx, String isFin, String listenIdx, String title, String nickName) {
            return LearningInfo.builder()
                    .learningInfoIdx(learningInfoIdx)
                    .isFin(isFin)
                    .listenIdx(listenIdx)
                    .title(title)
                    .nickName(nickName)
                    .build();
        }
    }
}
