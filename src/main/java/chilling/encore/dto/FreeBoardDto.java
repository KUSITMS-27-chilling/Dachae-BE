package chilling.encore.dto;

import chilling.encore.domain.FreeBoard;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class FreeBoardDto {
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
}
