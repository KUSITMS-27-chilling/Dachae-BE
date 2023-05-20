package chilling.encore.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOps;

    public void setValue(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public Optional<String> getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return Optional.ofNullable(values.get(key));
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public Optional<String> checkBlackList(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String isBlackList = values.get("blackList:" + token);
        return Optional.ofNullable(isBlackList);
    }

    public void addNotification(String userIdx, String notificationId, String title, String boardType, String boardIdx, String nickName, String content, String mention, LocalDateTime now) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        // 알림 내용을 문자열로 구성하여 저장
        String notification = boardType + ":" + boardIdx + ":" + title + ":" + nickName + ":" + content + ":" + mention + ":" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        // 알림을 개별 해시 필드로 추가
        hashOps.put("userIdx:" + userIdx + ":notifications", notificationId, notification);

        // 알림 Sorted Set에는 알림의 점수만 저장
        ZoneOffset offset = ZoneOffset.of("+09:00");
        zSetOps.add("userIdx:" + userIdx + ":notifications:sorted", notificationId, now.toEpochSecond(offset));
    }

    public Set<String> getNotificationIds(String userIdx, long start, long end) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        return zSetOps.range("userIdx:" + userIdx + ":notifications:sorted", start, end);
    }

    public List<String> getNotifications(String userIdx, Set<String> notificationIds) {
        return hashOps.multiGet("userIdx:" + userIdx + ":notifications", notificationIds);
    }

    public void removeNotification(String userIdx, String notificationId) {
        hashOps.delete("userIdx:" + userIdx + ":notifications", notificationId);
        redisTemplate.opsForZSet().remove("userIdx:" + userIdx + ":notifications:sorted", notificationId);
    }
}