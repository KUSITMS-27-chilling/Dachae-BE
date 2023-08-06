package chilling.encore.utils.domain;

import chilling.encore.domain.LearningCenter;
import chilling.encore.domain.Program;

import java.time.LocalDate;

public class MockProgram {
    public Program getMockProgram(Long idx, LearningCenter learningCenter, String name) {
        return Program.builder()
                .programIdx(idx)
                .learningCenter(learningCenter)
                .programName(name)
                .category("test")
                .url("tset")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
    }
}
