package com.example.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.user.dto.CreateUserRequest;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.entity.Wallet;
import com.example.user.exception.CustomException;
import com.example.user.exception.ResourceAlreadyExistException;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.UserRepository;
import com.example.user.repository.WalletRepository;
import com.example.user.utils.UserUtility;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserUtility userUtility;

    @Mock
    private WalletRepository walletRepository;

    private CreateUserRequest createUserRequest;
    private Wallet wallet;
    private User user;
    private Role role;

    @BeforeEach
    public void setup() {
        createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("john@yahoo.com");
        createUserRequest.setFirstName("John");
        createUserRequest.setLastName("Mark");
        createUserRequest.setPassword("12345678");
        createUserRequest.setPin("1234");

        role = new Role();
        role.setId(1L);
        role.setTitle("CONSUMER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

       wallet = new Wallet();
        wallet.setAccountNumber("42");
        wallet.setBalance(BigDecimal.valueOf(0L));
        wallet.setId(1L);
        wallet.setPin("Pin");

        user = new User();
        user.setEmail("john@yahoo.com");
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setId(1L);
        user.setLastName("Doe");
        user.setPassword("123456789");
        user.setRoles(roles);
        user.setWallet(wallet);
    }

    /**
     * Method under test: {@link UserServiceImpl#createUser(CreateUserRequest)}
     */
    @Test
    void givenCreateUser_whenUserExist_ResourceAlreadyExistException() throws ResourceAlreadyExistException {
        when(userRepository.existsByEmail((String) any())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> userServiceImpl.createUser(createUserRequest));
        verify(userRepository).existsByEmail((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#createUser(CreateUserRequest)}
     */
    @Test
    void givenCreaterUserRequest_whenCreateUser_thenReturnResponse() throws ResourceAlreadyExistException {

        when(userRepository.save((User) any())).thenReturn(user);
        when(userRepository.existsByEmail((String) any())).thenReturn(false);
        when(roleRepository.findByTitle((String) any())).thenReturn(Optional.of(new Role()));
        when(userUtility.from((CreateUserRequest) any())).thenReturn(user);


        ResponseEntity<String> actualCreateUserResult = userServiceImpl.createUser(createUserRequest);
        assertEquals("Consumer created", actualCreateUserResult.getBody());
        assertEquals(HttpStatus.CREATED, actualCreateUserResult.getStatusCode());
        verify(userRepository).existsByEmail((String) any());
        verify(userRepository).save((User) any());
        verify(roleRepository).findByTitle((String) any());
        verify(userUtility).from((CreateUserRequest) any());
        verify(rabbitMQMessageProducer).publish((Object) any(), (String) any(), (String) any());
    }


    /**
     * Method under test: {@link UserServiceImpl#createContentCreator(CreateUserRequest)}
     */
    @Test
    void givenCreateContentCreator_whenUserExist_ResourceAlreadyExistException() throws ResourceAlreadyExistException {
        when(userRepository.existsByEmail((String) any())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> userServiceImpl.createContentCreator(createUserRequest));
        verify(userRepository).existsByEmail((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#createContentCreator(CreateUserRequest)}
     */
    @Test
    void givenCreaterContentCreatorRequest_whenCreateContentCreator_thenReturnResponse() throws ResourceAlreadyExistException {

        when(userRepository.save((User) any())).thenReturn(user);
        when(userRepository.existsByEmail((String) any())).thenReturn(false);
        when(roleRepository.findByTitle((String) any())).thenReturn(Optional.of(new Role()));
        when(userUtility.from((CreateUserRequest) any())).thenReturn(user);


        ResponseEntity<String> actualCreateUserResult = userServiceImpl.createContentCreator(createUserRequest);
        assertEquals("Consumer created", actualCreateUserResult.getBody());
        assertEquals(HttpStatus.CREATED, actualCreateUserResult.getStatusCode());
        verify(userRepository).existsByEmail((String) any());
        verify(userRepository).save((User) any());
        verify(roleRepository).findByTitle((String) any());
        verify(userUtility).from((CreateUserRequest) any());
        verify(rabbitMQMessageProducer).publish((Object) any(), (String) any(), (String) any());
    }



    /**
     * Method under test: {@link UserServiceImpl#enableUser(Long)}
     */
    @Test
    void testEnableUser() throws ResourceNotFoundException {

        Optional<User> ofResult = Optional.of(user);

        when(userRepository.save((User) any())).thenReturn(user);
        when(userRepository.findById((Long) any())).thenReturn(ofResult);
        userServiceImpl.enableUser(1L);
        verify(userRepository).save((User) any());
        verify(userRepository).findById((Long) any());
    }



    /**
     * Method under test: {@link UserServiceImpl#getUserByAccountNumber(String)}
     */
    @Test
    void givenAccountNumber_whenGetUserByAccountNumber_thenReturnResponse() {

        when(userRepository.findByWalletId((Long) any())).thenReturn(user);

        when(walletRepository.findByAccountNumber((String) any())).thenReturn(wallet);
        ResponseEntity<User> actualUserByAccountNumber = userServiceImpl.getUserByAccountNumber("34");
        assertTrue(actualUserByAccountNumber.hasBody());
        assertEquals(HttpStatus.OK, actualUserByAccountNumber.getStatusCode());
        assertEquals("0", actualUserByAccountNumber.getBody().getWallet().getBalance().toString());
        verify(userRepository).findByWalletId((Long) any());
        verify(walletRepository).findByAccountNumber((String) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#walletBalanceUpdate(User, Long)}
     */
    @Test
    void givenWalletIdVersionAndBalance_whenWalletBalanceUpdate_thenReturnResponse() throws CustomException {
        when(walletRepository.updateWalletBalance((Long) any(), (Long) any(), (BigDecimal) any())).thenReturn(1);

        ResponseEntity<String> actualWalletBalanceUpdateResult = userServiceImpl.walletBalanceUpdate(user, 1L);
        assertEquals("Balance updated by transaction", actualWalletBalanceUpdateResult.getBody());
        assertEquals(HttpStatus.OK, actualWalletBalanceUpdateResult.getStatusCode());
        verify(walletRepository).updateWalletBalance((Long) any(), (Long) any(), (BigDecimal) any());
    }

    /**
     * Method under test: {@link UserServiceImpl#walletBalanceUpdate(User, Long)}
     */
    @Test
    void givenBadVersion_whenWalletBalanceUpdate_thenThrowCustomException() throws CustomException {
        when(walletRepository.updateWalletBalance((Long) any(), (Long) any(), (BigDecimal) any())).thenReturn(0);

        assertThrows(CustomException.class, () -> userServiceImpl.walletBalanceUpdate(user, 1L));
        verify(walletRepository).updateWalletBalance((Long) any(), (Long) any(), (BigDecimal) any());
    }


}

