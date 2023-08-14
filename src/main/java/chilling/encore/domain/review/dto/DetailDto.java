package chilling.encore.domain.review.dto;

import chilling.encore.domain.listenTogether.dto.ListenTogetherDto.SelectListenTogether;
import chilling.encore.domain.program.dto.ProgramDto.GetDetailPrograms;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public abstract class DetailDto {
    @Builder
    @Getter
    @ApiModel(description = "모아보기의 상세보기 접근을 위한 응답객체")
    public static class DetailResponse {
        private int favCount;
        private String tell;
        private int programPageTotal;
        private List<GetDetailPrograms> programs;
        private int listenTogetherPageTotal;
        private List<SelectListenTogether> listenTogethers;

        public static DetailResponse from(
                int favCount,
                String tell,
                List<GetDetailPrograms> programs,
                List<SelectListenTogether> listenTogethers,
                int programPageTotal,
                int listenTogetherPageTotal
        ) {
            return DetailResponse.builder()
                    .favCount(favCount)
                    .tell(tell)
                    .programs(programs)
                    .listenTogethers(listenTogethers)
                    .programPageTotal(programPageTotal)
                    .listenTogetherPageTotal(listenTogetherPageTotal)
                    .build();
        }
    }
}
