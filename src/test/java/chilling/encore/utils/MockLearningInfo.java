package chilling.encore.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MockLearningInfo {
    public Set<String> getIds() {
        Set<String> learningInfoIds = new HashSet<>();
        learningInfoIds.add("notification:" + 101);
        learningInfoIds.add("notification:" + 102);
        learningInfoIds.add("notification:" + 103);
        learningInfoIds.add("notification:" + 104);
        learningInfoIds.add("notification:" + 105);

        return learningInfoIds;
    }

    public List<String> getInfos() {
        List<String> learningInfos = Arrays.asList(
                "true:101:Learning title 1:Nick1",
                "false:102:Learning title 2:Nick2",
                "true:103:Learning title 3:Nick3",
                "false:104:Learning title 4:Nick4",
                "true:105:Learning title 5:Nick5"
        );

        return learningInfos;
    }
}
