package com.example.user.config;

import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    private Collection<? extends GrantedAuthority> authorities;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            //find app user by email
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
            //if the app user is present and is active, add the app user roles to the granted authorities and return app user
            if (optionalUser.isPresent()) {
                User userObj = optionalUser.get();
                if (userObj.isEnabled()) {
                    authorities = userObj.getAuthorities();
                    return userObj;
                }
                throw new UsernameNotFoundException("Account is disabled, please verify your email");
            }
            throw new UsernameNotFoundException("invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
