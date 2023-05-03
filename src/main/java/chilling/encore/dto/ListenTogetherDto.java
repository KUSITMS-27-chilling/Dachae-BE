package chilling.encore.dto;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public abstract class ListenTogetherDto {
    /**
     * 같이들어요에서
     * 보여줘야 하는 부분
     * listenTogetherIdx
     * isRecruiting(모집중인가)
     * UserProfile
     * UserNickname
     * title
     * UserFavField
     * goalNum (목표인원)
     * 모집된 인원 (participant에서)
     * programName
     * tag1
     * tag2
     */
    @Getter
    @Builder
    public static class SelectListenTogether {
        private Long listenTogetherIdx;
        private boolean isRecruiting;
        private String profile;
        private String nickName;
        private String title;
        private List<String> favFields;
        private int goalNum;
        private int currentNum;
        private String programName;
        private List<String> tags;

        public static SelectListenTogether from(
                boolean isRecruiting,
                User user,
                ListenTogether listenTogether,
                int currentNum,
                Program program
        ) {
            String[] split;
            List<String> favField = null;
            if (user.getFavField() != null) {
                split = user.getFavField().split(",");
                favField = List.of(split);
            }
            return SelectListenTogether.builder()
                    .isRecruiting(isRecruiting)
                    .listenTogetherIdx(listenTogether.getListenIdx())
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .title(listenTogether.getTitle())
                    .favFields(favField)
                    .goalNum(listenTogether.getGoalNum())
                    .currentNum(currentNum)
                    .programName(program.getProgramName())
                    .tags(List.of(program.getLearningCenter().getRegion(), program.getLearningCenter().getLearningName()))
                    .build();
        }
    }

    @Getter
    @Builder
    @ApiModel(description = "같이들어요 접근을 위한 응답객체")
    public static class ListenTogetherResponse {


        private List<PopularListenTogether> popularListenTogether;
        // 인기글
//        private List<ListenTogetherPosts> listenTogetherPosts;
        // 같이들어요 리스트
        // 내가 작성한 제안할래요 글 추가필요

        public static ListenTogetherResponse from(
                List<PopularListenTogether> popularListenTogether
        ) {
            return ListenTogetherResponse.builder()
                    .popularListenTogether(popularListenTogether)
                    .build();
        }
    }
    @Getter
    @Builder
    @ApiModel(description = "인기 같이들어요 3개 보여주기 위한 응답 객체")
    public static class PopularListenTogether {
        /**
         * 같이들어요 인기글에서 반환
         * listenIdx
         * title
         * hit
         * createdAt
         */

        private Long listenIdx;
        private String title;
        private int hit;
        private LocalDateTime createdAt;

        public static PopularListenTogether from(ListenTogether listenTogether) {
            return PopularListenTogether.builder()
                    .listenIdx(listenTogether.getListenIdx())
                    .title(listenTogether.getTitle())
                    .hit(listenTogether.getHit())
                    .createdAt(listenTogether.getCreatedAt())
                    .build();
        }
    }
}
