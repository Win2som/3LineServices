package com.example.notification.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public String generateConfirmationToken(Long account_id){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account_id
        );

        //save token
        confirmationTokenRepository.save(confirmationToken);

        return token;
    }


    public String confirmToken(String token, Long account_id){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if(!confirmationToken.getAccount_id().equals(account_id)){
            throw new IllegalStateException("token and account mismatch");
        }

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        setConfirmedAt(token);

        return "confirmed";
    }


    @Transactional
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.setConfirmationStatus(true);
        confirmationTokenRepository.save(confirmationToken);

    }


    public Boolean checkConfirmationTokenStatus(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow();
            return confirmationToken.isConfirmationStatus();
        }


}
