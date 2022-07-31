package com.example.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private final CustomUserDetailsService customUserDetailsService;
        private final JwtFilter jwtTokenAuthenticationFilter;


        @Autowired
        public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtFilter
                jwtTokenAuthenticationFilter) {
            this.customUserDetailsService = customUserDetailsService;
            this.jwtTokenAuthenticationFilter = jwtTokenAuthenticationFilter;

        }

        @Override
        public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(customUserDetailsService);

        }

        @Bean(BeanIds.AUTHENTICATION_MANAGER)
        public AuthenticationManager customAdminAuthenticationManager() throws Exception {
            return authenticationManager();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().
                    authorizeRequests()
                    .antMatchers("/user").permitAll()
                    .antMatchers("/user/create_content_creator").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/user/enable/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling().and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
