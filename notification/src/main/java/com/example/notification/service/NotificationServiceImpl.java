package com.example.notification.service;

import com.example.notification.dto.TransactionMailRequest;
import com.example.notification.dto.UserMailRequest;
import com.example.notification.email.EmailSender;
import com.example.notification.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;




@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

        private final ConfirmationTokenService confirmationTokenService;
        private final EmailSender emailSender;
        private final RestTemplate restTemplate;


        public void sendVerificationEmail(UserMailRequest mailRequest) {

            //call confirmationTokenService to generate token
            String token = confirmationTokenService.generateConfirmationToken(mailRequest.getId());

            String link = "localhost:8081/api/v1/notification/confirm/"+mailRequest.getId()+"?token="+ token;
            log.info("Sent link with token {}", link);
            //call emailSender to send email
            emailSender.send(mailRequest.getEmail(), buildVerificationEmail(mailRequest.getFirstName(), link), "Confirm your email");

        }



        private String buildVerificationEmail(String name, String link) {
            return
                    "<p>Hi " + name + ",</p>" +
                            "<p > Thank you for registering. Please click on the below link to activate your account: </p>" +
                            "<blockquote style=\"color:blue; font-size:25px;\"><a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. " +
                            "<p>See you soon</p>" +
                            "\n" +
                            "</div></div>";
        }


        @Transactional
        public String confirmToken(String token, Long user_id) {
            String confirmationResult = confirmationTokenService.confirmToken(token, user_id);

            String url = "http://localhost:8080/api/v1/user/enable/" + user_id;
            restTemplate.put(url, user_id, Long.class);

            return confirmationResult;
        }


        public String sendTransactionEmail(TransactionMailRequest mailRequest) {
            String email = buildTransactionEmail(mailRequest.getFirstName(), mailRequest.getAmount(), mailRequest.getTransactionType(), mailRequest.getCurrentBalance(), mailRequest.getStatus(), mailRequest.getDate());
            emailSender.send(mailRequest.getEmail(), email, getSubject(mailRequest.getTransactionType()));

            return "sent";
        }


        private String buildTransactionEmail(String name, BigDecimal amount, String transactionType, BigDecimal balance, String status, String date) {
            return
                    "<p>Hi " + name + ",</p>" +
                            "<p> The following transaction occurred in your account:</p>" +
                            "<p> Amount: "+ amount + "</p>" + "<p> Transaction type: "+ transactionType + "</p>" +
                            "<p> Balance: "+ balance + "</p>"+ "<p>Status: "+ status + "</p>"  + "<p>Date: "+ date +
                            "</p> <p>Thank you for trusting us</p>";
        }

        private String getSubject(String subject){
            log.info(subject);

            if(subject.equals("CREDIT")){
                return "Credit Transaction";
            }
            else{
                return "Debit Transaction";
            }
        }
}
