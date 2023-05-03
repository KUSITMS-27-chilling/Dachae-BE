package chilling.encore.dto;

import chilling.encore.dto.ProgramDto.GetDetailPrograms;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public abstract class MainDto {
    @Getter
    @Builder
    @ApiModel(description = "메인페이지 접근을 위한 응답객체")
    public static class MainResponse {
        private List<AlaramDto.NewAlarm> alarms;
        private List<ProgramDto.NewProgram> newPrograms;
        private Integer grade;
        private Map<String, ProgramDto.ProgramMainResponse> regionPrograms;
        public static MainResponse from(
                List<AlaramDto.NewAlarm> alarms,
                List<ProgramDto.NewProgram> newPrograms,
                Integer grade,
                Map<String, ProgramDto.ProgramMainResponse> regionPrograms
        ) {
            return MainResponse.builder()
                    .alarms(alarms)
                    .newPrograms(newPrograms)
                    .grade(grade)
                    .regionPrograms(regionPrograms)
                    .build();
        }
    }

    @Getter
    @Builder
    @ApiModel(description = "모아보기의 상세보기 접근을 위한 응답객체")
    public static class DetailResponse {
        private int favCount;
        private String tell;
        private List<GetDetailPrograms> programs;
        /**
         * 같이들어요 List 추가
         */
        public static DetailResponse from(
                int favCount,
                String tell,
                List<GetDetailPrograms> programs
        ) {
            return DetailResponse.builder()
                    .favCount(favCount)
                    .tell(tell)
                    .programs(programs)
                    .build();
        }
    }
}
