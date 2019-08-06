package com.sba.mail;

/**
 * @description:
 * @author: zjbing
 * @create: 2019-07-31 13:30
 **/
import com.sba.monitor.MonitorApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MonitorApplication.class)
public class MailTest {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Test
    public void testSendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo("xxxxxx@qq.com");
        simpleMailMessage.setSubject("ccccc");
        simpleMailMessage.setText("一一一一i");

        mailSender.send(simpleMailMessage);
    }

}
