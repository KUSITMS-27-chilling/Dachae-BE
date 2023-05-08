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
    public static class ListenTogetherDetail {
        private final String title;
        private final String programName;
        private final String createdAt;
        private final String content;
        private final String profile;
        private final String nickName;
        private final List<String> favField;
        private final int currentNum;
        private final int goalNum;
        private final List<ParticipantsInfo> participantsInfos;
        public static ListenTogetherDetail from(ListenTogether listenTogether, List<ParticipantsInfo> participantsInfos) {
            User user = listenTogether.getUser();
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));
            return ListenTogetherDetail.builder()
                    .title(listenTogether.getTitle())
                    .programName(listenTogether.getProgram().getProgramName())
                    .createdAt(listenTogether.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .content(listenTogether.getContent())
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .favField(favField)
                    .currentNum(participantsInfos.size())
                    .goalNum(listenTogether.getGoalNum())
                    .participantsInfos(participantsInfos)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ParticipantsInfo {
        private final String profile;
        private final String nickName;
        private final int age;
        private final List<String> favField;

        public static ParticipantsInfo from(User user) {
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));
            return ParticipantsInfo.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .age(user.getAge())
                    .favField(favField)
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class AllMyListenTogether {
        private final List<MyListenTogether> myListenTogethers;
        public static AllMyListenTogether from(List<MyListenTogether> myListenTogethers) {
            return AllMyListenTogether.builder()
                    .myListenTogethers(myListenTogethers)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class MyListenTogether {
        private final Long listenIdx;
        private final String title;
        public static MyListenTogether from(ListenTogether listenTogether) {
            return MyListenTogether.builder()
                    .listenIdx(listenTogether.getListenIdx())
                    .title(listenTogether.getTitle())
                    .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class AllPopularListenTogether {
        private final List<PopularListenTogether> popularListenTogethers;
        public static AllPopularListenTogether from(List<PopularListenTogether> popularListenTogethers) {
            return AllPopularListenTogether.builder()
                    .popularListenTogethers(popularListenTogethers)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class PopularListenTogether {
        private final Long listenIdx;
        private final String title;
        public static PopularListenTogether from(ListenTogether listenTogether) {
            return PopularListenTogether.builder()
                    .listenIdx(listenTogether.getListenIdx())
                    .title(listenTogether.getTitle())
                    .build();
        }
    }
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
        private final Long listenIdx;
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
                    .listenIdx(listenTogether.getListenIdx())
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
    @AllArgsConstructor
    @ApiModel(description = "같이 들어요 작성을 위한 요청 객체")
    public static class CreateListenTogetherRequest {
        private String title;
        private String region;
        private Long programIdx;
        private String programName;
        private int goalNum;
        private String content;
    }

}
