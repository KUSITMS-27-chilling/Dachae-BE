package chilling.encore.global.config.slack;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessage {
    private final Slack slackClient = Slack.getInstance();

    @Value("${slack.webhook.url}")
    private String webHookUrl;

    public void sendSlackAlertErrorLog(Exception e, String errorCode, HttpServletRequest request) {
        try {
            slackClient.send(webHookUrl, Payload.builder()
                    .text("서버 에러 발생!! 백엔드팀 확인 요망")
                    .attachments(
                            List.of(generateSlackAttachment(e, errorCode, request))
                    )
                    .build());
        } catch (IOException slackError) {
            log.error("Slack 통신 에러 발생");
        }
    }

    //attach 생성 -> Field를 리스트로 담자
    private Attachment generateSlackAttachment(Exception e, String errorCode, HttpServletRequest request) throws IOException {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS").format(LocalDateTime.now());
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null)
            ip = request.getRemoteAddr();
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + "에 발생한 에러 로그")
                .fields(List.of(
                        generateSlackField("Request IP", ip),
                        generateSlackField("Method", request.getMethod()),
                        generateSlackField("Request URL", String.valueOf(request.getRequestURL())),
                        generateSlackField("Error Code", errorCode),
                        generateSlackField("Error Message", e.getMessage()),
                        generateSlackField("StackTrace", getStackTraceAsString(e))
                ))
                .build();
    }

    // Field 생성 메서드
    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
