package chilling.global.config.email;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
@PropertySource("classpath:application.yml")
public class EmailConfig {
    /**
     * port, socketPort, auth, starttls... 등등
     * application.properties에 작성해놓은 내용을 기반으로 지정되게 된다.
     * 그리고 해당 내용을 기반으로 아래 설정이 완료된다.
     */
    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;
    @Value("${mail.AdminMail.id}")
    private String id;
    @Value("${mail.AdminMail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com"); //smtp 서버 주소
        javaMailSender.setUsername(id); // 발신자 메일 아이디
        javaMailSender.setPassword(password); // 발신 메일 패스워드(구글 앱 비밀번호)(따로 설정해야함)
        javaMailSender.setPort(port); //smtp port 구글, 네이버 등등에서 해당 메일의 port 확인 가능
        javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 인증서버 정보
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties pt = new Properties();
        pt.put("mail.smtp.socketFactory.port", socketPort); // smtp 포트 번호 넣어주는 것이다 (다른번호 사용시 설정 변경해야함)
        pt.put("mail.smtp.auth", auth); // stmp 권한 인증

        pt.put("mail.smtp.starttls.enable", starttls); //smtp starttls 사용
        pt.put("mail.smtp.starttls.required", startlls_required);
        //starttls -> 보안을 위한 사용(명시적 보안이라 함)
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return pt;
    }
}
