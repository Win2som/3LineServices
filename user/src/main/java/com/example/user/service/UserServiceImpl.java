package com.example.user.service;


import com.example.amqp.RabbitMQMessageProducer;
import com.example.user.dto.CreateUserRequest;
import com.example.user.dto.UserPayload;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.entity.Wallet;
import com.example.user.enums.RoleType;
import com.example.user.exception.CustomException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WalletRepository;
import com.example.user.utils.UserUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WalletRepository walletRepository;
    private final UserUtility userUtility;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;


public ResponseEntity<String> createUser(CreateUserRequest userRequest) throws ResourceAlreadyExistException {

    if (userRepository.existsByEmail(userRequest.getEmail())) {
        throw new ResourceAlreadyExistException("User already exist");
    }

    User user = userUtility.from(userRequest);
    user.setRoles(getRole(RoleType.CONSUMER));

    User savedUser = userRepository.save(user);

    UserPayload payload = UserPayload.builder()
            .firstName(savedUser.getFirstName())
            .email(savedUser.getEmail())
            .id(savedUser.getId())
            .build();
    //Call notification service to send mail with payload
    rabbitMQMessageProducer.publish(payload, "internal.notification.routing-key",
            "internal.exchange");

    return new ResponseEntity<>("Consumer created", HttpStatus.CREATED);

}


    @Override
    public ResponseEntity<String> createContentCreator(CreateUserRequest userRequest) throws ResourceAlreadyExistException {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceAlreadyExistException("User already exist");
        }
        User user = userUtility.from(userRequest);
        user.setRoles(getRole(RoleType.CONTENT_CREATOR));

        User savedUser = userRepository.save(user);

        UserPayload payload = UserPayload.builder()
                .firstName(savedUser.getFirstName())
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .build();

        //Call notification service to send mail with payload
        rabbitMQMessageProducer.publish(payload, "internal.notification.routing-key",
                "internal.exchange");

        return new ResponseEntity<>("Content Creator created", HttpStatus.CREATED);

    }



//    private Role getRole(RoleType roleType) {
//        Optional<Role> optionalRole = roleRepository.findByName(roleType.name());
//        return optionalRole
//                .orElseGet(() -> roleRepository.save(Role.builder()
//                        .title(roleType.name())
//                        .build()));
//    }

    private Set<Role> getRole(RoleType roleType) {
        Optional<Role> optionalRole = roleRepository.findByTitle(roleType.name());
        Role role;
        Set<Role> roles = new HashSet<>();
        if(optionalRole.isEmpty()){
            role = roleRepository.save(Role.builder()
                    .title(roleType.name())
                    .build());
        }
        else{
            role = optionalRole.get();
        }
        roles.add(role);
        return roles;
    }



    @Override
    public void enableUser(Long id) throws ResourceNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s not found",id)));
        user.setEnabled(true);
        userRepository.save(user);
    }



    @Override
    public ResponseEntity<User> getUserByAccountNumber(String accountNum) {
        log.info(accountNum);
        Optional<Wallet> wallet = Optional.ofNullable(walletRepository.findByAccountNumber(accountNum));
        User user = null;
        if (wallet.isPresent()) {

            user = userRepository.findByWalletId(wallet.get().getId());
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> walletBalanceUpdate(User user, Long id) throws CustomException {
        log.info("wallet balance {}", user.getWallet().getBalance());

        int updatedCount = walletRepository.updateWalletBalance(user.getWallet().getId(), user.getWallet().getVersion(), user.getWallet().getBalance());
        if (updatedCount < 1) throw new CustomException("Please try again !!!");

        return new ResponseEntity<>("Balance updated by transaction",HttpStatus.OK);
    }


}
