package chilling.encore.utils.domain;

import chilling.encore.domain.LearningCenter;

public class MockLearningCenter {
    public LearningCenter getMockLearningCenter(Long idx, String region, String name) {
        return LearningCenter.builder()
                .learningCenterIdx(idx)
                .region(region)
                .learningName(name)
                .x(1)
                .y(1)
                .build();
    }
}
