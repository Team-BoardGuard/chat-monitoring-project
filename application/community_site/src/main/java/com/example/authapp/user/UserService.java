package com.example.authapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setWarnCount(0L);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public void updateProfile(String username, ProfileEditForm profileEditForm) {
        User user = findByUsername(username);
        // Username should not be changed, only update the nickname
        user.setNickname(profileEditForm.getNickname());
        userRepository.save(user);
    }

    public boolean changePassword(String username, PasswordChangeForm form) {
        User user = findByUsername(username);

        // Check if the current password is correct
        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            return false; // Current password does not match
        }

        // Check if the new password and confirmation match
        if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
            return false; // New passwords do not match
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);

        return true;
    }

    public void deleteUser(String username) {
        User user = findByUsername(username);
        user.setRole(Role.DELETED);
        user.setNickname("탈퇴한 사용자");
        user.setEmail("deleted_" + user.getId() + "@example.com"); // Maintain uniqueness
        // Set a random, unusable password
        user.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        if (user.getRole() == Role.DELETED) {
            throw new DisabledException("User with username " + username + " has been deleted.");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singleton(authority));
    }
}