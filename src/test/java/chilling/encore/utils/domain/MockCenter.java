package chilling.encore.utils.domain;

import chilling.encore.domain.center.entity.Center;
import lombok.Data;

@Data
public class MockCenter extends Center{
    public MockCenter(Long regionIdx, String url, String region, int favCount, String tell) {
        this.regionIdx = regionIdx;
        this.url = url;
        this.region = region;
        this.favCount = favCount;
        this.tell = tell;
    }
    private Long regionIdx;
    private String url;
    private String region;
    private int favCount;
    private String tell;

    public void plusFavCount() {
        favCount++;
    }
}
