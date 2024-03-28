package com.nick.jakartaproject.email;

import com.nick.jakartaproject.form.FormOrder;
import com.nick.jakartaproject.form.FormContact;
import com.nick.jakartaproject.models.dao.AreaDAO;
import com.nick.jakartaproject.models.dao.PieDAO;
import com.nick.jakartaproject.models.domain.OrderItem;
import com.nick.jakartaproject.models.domain.Pie;
import com.nick.jakartaproject.models.domain.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Email {
    public static void sendTextEmail(String recipientToEmail, String subject, String body) {
        // read properties
        Properties properties = null;
        try (InputStream is = Email.class.getClassLoader().getResourceAsStream("mail.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create session
        Properties finalProperties = properties;
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                finalProperties.getProperty("username"),
                                finalProperties.getProperty("password")
                        );
                    }
                }
        );

        // send e-mail
        Message mimeMessage = new MimeMessage(session);
        try {
            // prepare message
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);

            // declare recipients
            Address from = new InternetAddress(properties.getProperty("mail.admin"));
            Address to = new InternetAddress(recipientToEmail);

            mimeMessage.setFrom(from);
            mimeMessage.setRecipient(Message.RecipientType.TO, to);

            // send the message
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendHtmlEmail(String recipientToEmail, String subject, String body) {
        // read properties
        Properties properties = null;
        try (InputStream is = Email.class.getClassLoader().getResourceAsStream("mail.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create session
        Properties finalProperties = properties;
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                finalProperties.getProperty("username"),
                                finalProperties.getProperty("password")
                        );
                    }
                }
        );

        // send e-mail
        Message mimeMessage = new MimeMessage(session);
        try {
            // prepare message
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(body, "text/html; charset=UTF-8");

            // declare recipients
            Address from = new InternetAddress(properties.getProperty("mail.admin"));
            Address to = new InternetAddress(recipientToEmail);

            mimeMessage.setFrom(from);
            mimeMessage.setRecipient(Message.RecipientType.TO, to);

            // send the message
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendEmailToClientContactForm(FormContact formData) {
        String text =
                "We received your message with the following information: \n" +
                        "\tFirst and last name: " + formData.getFullname() + "\n" +
                        "\tE-mail: " + formData.getEmail() + "\n" +
                        "\tPhone: " + formData.getTel() + "\n" +
                        "\tMessage: " + formData.getMessage() + "\n" +
                        "and we will contact you soon!";

        sendTextEmail(formData.getEmail(), "Contact Update", text);;
    }
    public static void sendEmailToAdminContactForm(FormContact formData) {
        // read properties
        Properties properties = null;
        try (InputStream is = Email.class.getClassLoader().getResourceAsStream("mail.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text =
                "New message from the contact form on our site: \n" +
                        "\tFirst name: " + formData.getFullname() + "\n" +
                        "\tE-mail: " + formData.getEmail() + "\n" +
                        "\tPhone: " + formData.getTel() + "\n" +
                        "\tMessage: " + formData.getMessage() + "\n";


        sendTextEmail(properties.getProperty("mail.admin"), "Incoming Message from the contact form", text);     }

    public static void sendEmailToClientOrder(FormOrder formOrder) {
        List<Pie> pies = PieDAO.getPies();

        double[] values = new double[pies.size()];
        int index = 0;
        StringBuilder tableRows = new StringBuilder();

        for (OrderItem orderItem : formOrder.getOrderItems()) {
            Pie pie = pies.get(index);
            int quantity = orderItem.getQuantity();
            double value = quantity * pie.getPrice();
            values[index] = value;

            String rowColor = index % 2 == 0 ? "" : "background-color: #ded8dd";

            tableRows.append("<tr style=\"" + rowColor + "\">\n")
                    .append("<td style=\"text-align: center;border: 0;padding: 30px;font-weight: normal;\">" + pie.getName() + "</td>\n")
                    .append("<td style=\"text-align: center;border: 0;padding: 30px;font-weight: normal;\">" + quantity + "</td>\n")
                    .append("<td style=\"text-align: center;border: 0;padding: 30px;font-weight: normal;\">" + String.format("%.2f", value) + "€</td>\n")
                    .append("</tr>\n");

            index++;
        }

        double sum = Arrays.stream(values).sum();
        LocalDateTime timestamp = formOrder.getTimestamp();
        LocalDateTime timestampUntil = timestamp.plus(30, ChronoUnit.MINUTES);

        String text =
                "<header style=\"padding: 30px;text-align: center;font-size: 20px;font-weight: bold;\">Order on route</header>\n" +
                        "<table class=\"table-pies\" style=\"border-collapse: collapse;vertical-align: center;caption-side: bottom;margin: 0 auto;box-shadow: 0 0 4px 1px #483C46;width: 90%;\">\n" +
                        "  <thead>\n" +
                        "  <tr style=\"background-image: linear-gradient(to bottom, #483C46, #675664);font-size: 1em;margin-bottom: 10px;color: #BEEE62;\">\n" +
                        "    <th style=\"padding: 10px 20px;border: 0;\">Pie</th>\n" +
                        "    <th style=\"padding: 10px 20px;border: 0;\">Quantity</th>\n" +
                        "    <th style=\"padding: 10px 20px;border: 0;\">Price</th>\n" +
                        "  </tr>\n" +
                        "  </thead>\n" +
                        "  <tbody>\n" +
                        tableRows.toString() +
                        "  </tbody>\n" +
                        "  <tfoot>\n" +
                        "  <tr>\n" +
                        "    <td colspan=\"3\" style=\"padding: 10px;font-weight: bold;font-size: 20px;\">Σύνολο: " + String.format("%.2f", sum) + "€\n" +
                        "    </td>\n" +
                        "  </tr>\n" +
                        "  </tfoot>\n" +
                        "</table>\n" +
                        "<p style=\"padding: 30px;text-align: center;font-size: 20px;font-weight: bold;\">Estimated Delivery Time: " +
                        timestampUntil.format(DateTimeFormatter.ofPattern("d/M/u (kk:mm:ss)")) +
                        "</p>";

        sendHtmlEmail(formOrder.getEmail(), "Your Order!", text);
    }


    public static void sendEmailToAdminOrder(FormOrder formData) {
        // read properties
        Properties properties = null;
        try (InputStream is = Email.class.getClassLoader().getResourceAsStream("mail.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text =
                "New order: \n" +
                        "\tName: " + formData.getFullName() + "\n" +
                        "\tAddress: " + formData.getAddress() + "\n" +
                        "\tArea: " + AreaDAO.getAreaById(formData.getAreaId()).getDescription() + "\n" +
                        "\tEmail: " + formData.getEmail() + "\n" + "\n" +
                        "\tPhone: " + formData.getTel() + "\n" + "\n" +
                        "\tMessage: " + formData.getComments() + "\n" +
                        "\tOrder: \n" +
                        "\tSpinachPie: " + formData.getOrderItems().get(0).getQuantity() + "\n" +
                        "\tMushroom pies: " + formData.getOrderItems().get(1).getQuantity() + "\n" +
                        "\tCherry pies: " + formData.getOrderItems().get(1).getQuantity() + "\n" +
                        "\tBuns: " + formData.getOrderItems().get(1).getQuantity() + "\n" +
                        "\tReport: " + formData.getOffer() + "\n" +
                        "\tPayment Method: " + formData.getPayment() + "\n";

        sendTextEmail(properties.getProperty("mail.admin"), "New Order", text);
    }
    public static void sendAuthenticationEmailToClient(User user) {
        String text =
                "<div> Follow the link below to complete your registration: "+
                        "<a href=\"http://localhost:8080/register?code=" + user.getCode() + "\">" +
                        "http://localhost:8080/register?code=" + user.getCode() + "</a>" +
                "</div>";

        sendHtmlEmail(user.getEmail(), "Registration Verification", text);
    }
    public static void sendResetPasswordEmailToClient(String email, String code) {
        String text =
                "<div>Follow the link below to complete your registration: "+
                        "<a href=\"http://localhost:8080/change-password?code=" + code + "\">" +
                        "http://localhost:8080/change-password?code=" + code + "</a>" +
                        "</div>";

        sendHtmlEmail(email, "Reset Password on the page", text);
    }
}
