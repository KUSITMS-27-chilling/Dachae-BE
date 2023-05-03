package chilling.encore.dto;

import chilling.encore.domain.Program;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public abstract class ProgramDto {

    @Getter
    @Builder
    @ApiModel(description = "프로그램 3개 보여주기 위한 응답 객체")
    public static class ProgramMainResponse {
        private int favCount;
        private List<getPrograms> programs;

        public static ProgramMainResponse from(int favCount, List<getPrograms> programs) {
            return ProgramMainResponse.builder()
                    .programs(programs)
                    .favCount(favCount)
                    .build();
        }
    }
    @Getter
    @Builder
    public static class getPrograms {
        private Long programIdx;
        private String programName;
        private String category;
        private String url;
        private LocalDate endDate;

        public static ProgramDto.getPrograms from(Program program) {
            return getPrograms
                    .builder()
                    .programIdx(program.getProgramIdx())
                    .programName(program.getProgramName())
                    .category(program.getCategory())
                    .url(program.getUrl())
                    .endDate(program.getEndDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NewProgram {
        private Long programIdx;
        private String programName;
        private String url;
        private LocalDate startDate;
        private LocalDate endDate;

        public static NewProgram from(Program program) {
            return NewProgram
                    .builder()
                    .programIdx(program.getProgramIdx())
                    .programName(program.getProgramName())
                    .url(program.getUrl())
                    .startDate(program.getStartDate())
                    .endDate(program.getEndDate())
                    .build();
        }
    }
}
