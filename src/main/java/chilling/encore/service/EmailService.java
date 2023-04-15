package chilling.encore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;
    private final String E_KEY = createKey();

    private MimeMessage createMessage(String to)throws Exception{
        log.info("보내는 대상 : {}", to);
        log.info("인증 번호 : " + E_KEY);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상 (받은 email이 들어감)
        message.setSubject("ENCORE 회원가입 인증번호");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> TALER </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= E_KEY +"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ywj9811@gmail.com","TALER"));
        //보내는 사람 메일 주소, 보여질 이름

        return message;
    }

    private String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    public String sendSimpleMessage(String to)throws Exception {
        MimeMessage message = createMessage(to);
        try{//예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return E_KEY;
    }
}
