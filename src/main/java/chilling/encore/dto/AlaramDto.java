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
        private String notificationId;
        private String boardType;
        private String boardIdx;
        private String mention;
        private String title;
        private String nickName;
        private String content;
        private String createdAt;

        public static NewAlarm from(String notificationId, String boardType, String boardIdx,
                                    String mention, String title, String nickName, String content, String createdAt) {
            return NewAlarm.builder()
                    .notificationId(notificationId)
                    .boardType(boardType)
                    .boardIdx(boardIdx)
                    .mention(mention)
                    .title(title)
                    .nickName(nickName)
                    .content(content)
                    .createdAt(createdAt)
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
