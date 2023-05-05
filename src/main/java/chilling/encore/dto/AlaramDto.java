package chilling.encore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public abstract class AlaramDto {
    @Getter
    @Builder
    public static class NewAlarm {
        private Long listenCommentsIdx;
        private Long reviewCommentIdx;
        private String nickName;

        public static NewAlarm from(Long listenCommentsIdx, Long reviewCommentIdx, String nickName) {
            return NewAlarm.builder()
                    .listenCommentsIdx(listenCommentsIdx)
                    .reviewCommentIdx(reviewCommentIdx)
                    .nickName(nickName)
                    .build();
        }
    }

    @Getter
    @Builder
    @ApiModel(description = "알람 확인을 위한 응답 객체")
    public static class AlarmResponse {
        private List<NewAlarm> alarms;
        public static AlarmResponse from(List<NewAlarm> alarms) {
            return AlarmResponse.builder()
                    .alarms(alarms)
                    .build();
        }
    }
}
