package chilling.encore.utils;

import chilling.encore.domain.Center;

import java.util.List;

public class MockCenter {
    Center first = new Center(1L, "a", "강서구", 10, "1");
    Center second = new Center(2L, "b", "강남구", 10, "2");
    Center third = new Center(3L, "c", "동작구", 10, "3");
    Center fourth = new Center(4L, "d", "노원구", 10, "4");

    public List<Center> fourCenters = List.of(first, second, third, fourth);
}
