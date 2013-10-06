package com.awwa.sendgrid.hellosendgrid;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import jp.co.flect.sendgrid.SendGridClient;
import jp.co.flect.sendgrid.SendGridException;
import jp.co.flect.sendgrid.model.Bounce;
import jp.co.flect.sendgrid.model.Profile;
import jp.co.flect.sendgrid.model.Statistic;
import jp.co.flect.sendgrid.model.Unsubscribe;
import jp.co.flect.sendgrid.model.WebMail;



public class HelloSendGrid { 
    @SuppressWarnings("unused")
    private static final String TAG = HelloSendGrid.class.getSimpleName();
    private final HelloSendGrid self = this;

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "ここをSendGridのアカウントに置き換える";
    private static final String SMTP_AUTH_PWD = "ここをSendGridのパスワードに置き換える";
    
    private static final String SENDER_ADDRESS = "ここを送信元メールアドレスに置き換える";
    private static final String RECIPIENT_ADDRESS = "ここを宛先メールアドレスに置き換える";
    private static final String EDIT_ADDRESS = "momomo@momomo.jp";
    
    public String sendSMTP() throws MessagingException {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.port", 2525);
        props.put("mail.smtp.auth", "true");
        
        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();
        
        MimeMessage message = new MimeMessage(mailSession);
        
        Multipart multipart = new MimeMultipart("alternative");
        
        BodyPart part1 = new MimeBodyPart();
        part1.setText("This is multipart mail and u read part1.......そしてここからが日本語本文でござる");
        
        BodyPart part2 = new MimeBodyPart();
        part2.setContent("<b>This is multipart mail and u read part2.......そしてこれがマルチパートコンテンツ部の日本語でござる</b>", "text/html;charset=iso-2022-jp");
        
        multipart.addBodyPart(part1);
        multipart.addBodyPart(part2);
        
        message.setContent(multipart);
        message.setFrom(new InternetAddress(SENDER_ADDRESS));
        message.setSubject("This is the subject。タイトルも日本語行けるのかな");
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(RECIPIENT_ADDRESS));
        
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
        
        return "SMTP向けに送信した";
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator { 
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
    
    public String sendWebAPI() throws IOException, SendGridException {
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        WebMail mail = new WebMail();
        mail.setFrom(SENDER_ADDRESS);
        mail.setTo(RECIPIENT_ADDRESS);
        mail.setFromName("送り主");
        mail.setToName("宛て先");
        
        mail.setSubject("こんにちはせんどーぐりっど");
        mail.setText("おれもせんどーぐりっど");
        
        mail.setCategory("category111");
        
        client.mail(mail);
        
        return "WebAPI向けに送信した";
    }
    
    public String printBounces() throws IOException, SendGridException {
        StringBuilder sb = new StringBuilder();
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Bounce.Get request = new Bounce.Get();
        request.setDays(7);
        List<Bounce> list = client.getBounces(request);
        for (Bounce bounce : list) {
            sb.append(bounce.getEmail());
        }
        return sb.toString();
    }
    
    public String printUnsubscribes() throws IOException, SendGridException {
        StringBuilder sb = new StringBuilder();
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Unsubscribe.Get request = new Unsubscribe.Get();
        request.setDays(7);
        List<Unsubscribe> list = client.getUnsubscribes(request);
        for (Unsubscribe unsubscribe : list) {
            sb.append(unsubscribe.getEmail());
            sb.append(unsubscribe.getCreated());
        }
        return sb.toString();
    }
    
    public String addUnsubscribes() throws IOException, SendGridException {
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Unsubscribe.Add request = new Unsubscribe.Add();
        request.setEmail(EDIT_ADDRESS);
        client.addUnsubscribes(request);
        return "追加した: " + EDIT_ADDRESS;
    }
    
    public String deleteUnsubscribes() throws IOException, SendGridException {
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Unsubscribe.Delete request = new Unsubscribe.Delete();
        request.setEmail(EDIT_ADDRESS);
        client.deleteUnsubscribes(request);
        return "削除した: " + EDIT_ADDRESS;
    }
    
    public String printStatistics() throws IOException, SendGridException {
        StringBuilder sb = new StringBuilder();
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Statistic.Get request = new Statistic.Get();
        request.setDays(10);
        List<Statistic> list= client.getStatistics(request);
        for (Statistic stat : list) {
            sb.append("Statistics: " + stat.getDate() + "\n");
            sb.append("Category = " + stat.getCategory() + "\n" );
            sb.append("Requests = " + stat.getRequests() + "\n" );
            sb.append("Bounces = " + stat.getBounces() + "\n" );
            sb.append("Clicks = " + stat.getClicks() + "\n" );
            sb.append("Opens = " + stat.getOpens() + "\n" );
            sb.append("SpamReports = " + stat.getSpamReports() + "\n" );
            sb.append("UniqueClicks = " + stat.getUniqueClicks() + "\n" );
            sb.append("UniqueOpens = " + stat.getUniqueOpens() + "\n" );
            sb.append("Blocked = " + stat.getBlocked() + "\n" );
            sb.append("Delivered = " + stat.getDelivered() + "\n" );
            sb.append("Unsubscribes = " + stat.getUnsubscribes() + "\n" );
            sb.append("InvalidEmails = " + stat.getInvalidEmails() + "\n" );
            sb.append("RepeatUnsubscribes = " + stat.getRepeatUnsubscribes() + "\n" );
            sb.append("SpamDrops = " + stat.getSpamDrops() + "\n" );
            sb.append("RepeatBounces = " + stat.getRepeatBounces() + "\n" );
            sb.append("RepeatSpamReports = " + stat.getRepeatSpamReports() + "\n" );
        }
        return sb.toString();
    }
    
    public String printProfile() throws IOException, SendGridException {
        StringBuilder sb = new StringBuilder();
        SendGridClient client = new SendGridClient(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        Profile profile = client.getProfile();
        sb.append("Email = " + profile.getEmail() + "\n");
        sb.append("Address = " + profile.getAddress() + "\n");
        sb.append("Address2 = " + profile.getAddress2() + "\n");
        return sb.toString();
    }
}
