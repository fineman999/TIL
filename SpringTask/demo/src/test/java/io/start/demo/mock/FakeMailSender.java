package io.start.demo.mock;


import io.start.demo.user.service.port.MailSender;

public class FakeMailSender implements MailSender {

    public String email;
    public String title;
    public String content;

    @Override
    public void send(String email, String title, String content) {
        this.title = title;
        this.email = email;
        this.content = content;
    }
}
