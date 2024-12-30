package com.example.myapplication;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public static void sendEmail(String toEmail, String subject, String body) {
        // Configurații pentru serverul SMTP Gmail
        final String fromEmail = "aletalapan@gmail.com"; // Emailul tău
        final String password = "tuow fcll nybg nups"; // Parola ta

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Server SMTP
        props.put("mail.smtp.port", "465"); // Port SMTP
        props.put("mail.smtp.auth", "true"); // Autentificare necesară
        props.put("mail.smtp.socketFactory.port", "465"); // Port securizat
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Creare mesaj
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Trimitere mesaj
            Transport.send(message);
            System.out.println("Email trimis cu succes către: " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Eroare la trimiterea emailului: " + e.getMessage());
        }
    }
}
