package com.example.backend.security;

import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username for authentication
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                getAuthorities(user)
        );
    }

    /**
     * Load user by email for authentication
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                getAuthorities(user)
        );
    }

    /**
     * Load user by userId
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                getAuthorities(user)
        );
    }

    /**
     * Get user authorities based on account type and KYC status
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Base role for all authenticated users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Role based on account type
        if ("institutional".equalsIgnoreCase(user.getAccountType())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_INSTITUTIONAL"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_INDIVIDUAL"));
        }

        // Role based on KYC status
        if ("approved".equalsIgnoreCase(user.getKycStatus())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_VERIFIED"));
        } else if ("pending".equalsIgnoreCase(user.getKycStatus())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PENDING_VERIFICATION"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_UNVERIFIED"));
        }

        // Admin role (if applicable)
        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }
}