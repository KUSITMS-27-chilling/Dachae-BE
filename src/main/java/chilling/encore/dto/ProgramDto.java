package chilling.encore.dto;

import chilling.encore.domain.Program;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public abstract class ProgramDto {
    @Getter
    @Builder
    @ApiModel(description = "프로그램 새소식 위한 응답 객체")
    public static class NewProgramsResponse {
        private List<NewProgram> newPrograms;

        public static NewProgramsResponse from(List<NewProgram> newPrograms) {
            return NewProgramsResponse.builder()
                    .newPrograms(newPrograms)
                    .build();
        }
    }

    @Getter
    @Builder
    @ApiModel(description = "상세보기 프로그램을 위한 응답 객체")
    public static class PagingPrograms {
        private int totalProgramPage;
        private List<GetDetailPrograms> programs;

        public static PagingPrograms from(int totalProgramPage, List<GetDetailPrograms> programs) {
            return PagingPrograms.builder()
                    .totalProgramPage(totalProgramPage)
                    .programs(programs)
                    .build();
        }
    }
    
    @Getter
    @Builder
    @ApiModel(description = "ProgramMainResponse 모음 응답")
    public static class AllProgramMainResponses {
        Map<String, ProgramMainResponse> programMainResponses;

        public static AllProgramMainResponses from(Map<String, ProgramMainResponse> programMainResponses) {
            return AllProgramMainResponses.builder()
                    .programMainResponses(programMainResponses)
                    .build();
        }
    }
    
    @Getter
    @Builder
    @ApiModel(description = "프로그램 3개와 정보")
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
    @ApiModel(description = "센터의 모든 프로그램 정보")
    public static class AllProgramInCenter {
        private List<GetProgramTitle> programs;

        public static AllProgramInCenter from(List<GetProgramTitle> programs) {
            return AllProgramInCenter.builder()
                    .programs(programs)
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
    public static class GetDetailPrograms {
        private Long programIdx;
        private String programName;
        private String category;
        private String url;

        public static ProgramDto.GetDetailPrograms from(Program program) {
            return GetDetailPrograms
                    .builder()
                    .programIdx(program.getProgramIdx())
                    .programName(program.getProgramName())
                    .category(program.getCategory())
                    .url(program.getUrl())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class GetProgramTitle {
        private Long programIdx;
        private String programName;
        public static GetProgramTitle from(Program program) {
            return GetProgramTitle
                    .builder()
                    .programIdx(program.getProgramIdx())
                    .programName(program.getProgramName())
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
