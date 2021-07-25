package com.java.springportfolio.service;

import com.java.springportfolio.dao.UserRepository;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.UserNotActivatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ItemNotFoundException("No user found with the name: " + username));
        if (!user.isEnabled()) {
            log.error("Account with username: '{}' has not been activated!", user.getUsername());
            throw new UserNotActivatedException("Your account has not been activated");
        }
        return new org.springframework.security.core
                .userdetails.User(user.getUsername(), user.getPassword(), true, true, true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
