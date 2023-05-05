package chilling.encore.dto;

import chilling.encore.domain.Center;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

public abstract class CenterDto {
    @Getter
    @Builder
    @ApiModel(description = "센터 정보")
    public static class CenterInfo {
        private int favCount;
        private String tell;

        public static CenterInfo from(Center center) {
            return CenterInfo.builder()
                    .favCount(center.getFavCount())
                    .tell(center.getTell())
                    .build();
        }
    }
}
