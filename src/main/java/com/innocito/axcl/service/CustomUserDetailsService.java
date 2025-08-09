package com.innocito.axcl.service;

import com.innocito.axcl.entity.User;
import com.innocito.axcl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository repo;

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email);
    }

}