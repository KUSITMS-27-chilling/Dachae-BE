package chilling.encore.utils.domain;

import chilling.encore.domain.center.entity.Center;
import lombok.Data;

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

    @Override
    public Long getRegionIdx() {
        return regionIdx;
    }

    public void setRegionIdx(Long regionIdx) {
        this.regionIdx = regionIdx;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    @Override
    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }
}
