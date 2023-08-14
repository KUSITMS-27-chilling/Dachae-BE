package chilling.encore.domain.freeBoard.dto;

import chilling.encore.domain.freeBoard.entity.FreeBoard;
import chilling.encore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class FreeBoardDto {
    @Getter
    @RequiredArgsConstructor
    public static class CreateFreeBoardRequest {
        private final String title;
        private final String region;
        private final String content;

        public static FreeBoard to(CreateFreeBoardRequest createFreeBoardRequest, User user) {
            return FreeBoard.builder()
                    .title(createFreeBoardRequest.title)
                    .region(createFreeBoardRequest.region)
                    .content(createFreeBoardRequest.content)
                    .user(user)
                    .build();
        }
    }
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class PopularFreeBoards {
        private final List<String> popularFreeBoards;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class AllFreeBoards {
        private final List<SelectFreeBoard> freeBoards;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class SelectFreeBoard {
        private final Long freeBoardIdx;
        private final String profile;
        private final String nickName;
        private final String createdAt;
        private final String title;
        private final String content;
        private final int hit;
        private final int comments;
        public static SelectFreeBoard from(FreeBoard freeBoard) {
            return SelectFreeBoard.builder()
                    .freeBoardIdx(freeBoard.getFreeBoardIdx())
                    .profile(freeBoard.getUser().getProfile())
                    .nickName(freeBoard.getUser().getNickName())
                    .createdAt(freeBoard.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .hit(freeBoard.getHit())
                    .comments(freeBoard.getFreeBoardComments().size())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class FreeBoardDetail {
        private final String profile;
        private final String nickName;
        private final int grade;
        private final List<String> favFields;
        private final String createdAt;
        private final String title;
        private final String content;

        public static FreeBoardDetail from(FreeBoard freeBoard) {
            User user = freeBoard.getUser();
            List<String> favField = new ArrayList<>();
            if (user.getFavField() != null)
                favField = List.of(user.getFavField().split(","));

            return FreeBoardDetail.builder()
                    .profile(user.getProfile())
                    .nickName(user.getNickName())
                    .grade(user.getGrade()/10 + 1)
                    .favFields(favField)
                    .createdAt(freeBoard.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")))
                    .title(freeBoard.getTitle())
                    .content(freeBoard.getContent())
                    .build();
        }
    }
}
