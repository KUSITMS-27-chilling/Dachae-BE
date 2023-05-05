package chilling.encore.dto;

import chilling.encore.domain.ListenTogether;
import chilling.encore.domain.Program;
import chilling.encore.domain.User;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class ListenTogetherDto {
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ListenTogetherPage {
        private final int totalListenTogetherPage;
        private final List<SelectListenTogether> listenTogethers;
        public static ListenTogetherPage from(int totalListenTogetherPage, List<SelectListenTogether> listenTogethers) {
            return ListenTogetherPage.builder()
                    .totalListenTogetherPage(totalListenTogetherPage)
                    .listenTogethers(listenTogethers)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SelectListenTogether {
        private final Long listenTogetherIdx;
        private final boolean isRecruiting;
        private final String profile;
        private final String nickName;
        private final String createdAt;
        private final String title;
        private final List<String> favFields;
        private final int goalNum;
        private final int currentNum;
        private final String programName;
        private final List<String> tags;
        public static SelectListenTogether from(
                boolean isRecruiting,
                ListenTogether listenTogether,
                int currentNum
        ) {
            User user = listenTogether.getUser();
            Program program = listenTogether.getProgram();
            String[] split;
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null) {
                split = user.getFavField().split(",");
                favField = List.of(split);
            }
            return SelectListenTogether.builder()
                    .isRecruiting(isRecruiting)
                    .listenTogetherIdx(listenTogether.getListenIdx())
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .createdAt(listenTogether.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
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
