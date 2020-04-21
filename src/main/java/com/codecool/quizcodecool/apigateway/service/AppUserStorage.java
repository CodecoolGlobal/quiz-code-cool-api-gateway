package com.codecool.quizcodecool.apigateway.service;

import com.codecool.quizcodecool.apigateway.model.AppUser;
import com.codecool.quizcodecool.apigateway.model.UserCredentials;
import com.codecool.quizcodecool.apigateway.model.exception.EmailAlreadyExistException;
import com.codecool.quizcodecool.apigateway.model.exception.UsernameAlreadyExistException;
import com.codecool.quizcodecool.apigateway.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppUserStorage {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    EmailSenderService emailSenderService;

    public void add(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser getByName(String name) {
        return appUserRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not found"));
    }

    public boolean signUp(UserCredentials userCredentials) throws AuthenticationException {

        String username = userCredentials.getUsername();
        String email = userCredentials.getEmail();

        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistException();
        }
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistException();
        }
        appUserRepository.save(AppUser
                .builder()
                .username(username)
                .password(encoder.encode(userCredentials.getPassword()))
                .role("USER")
                .email(email)
                .registrationDate(LocalDate.now())
                .build()
        );
        emailSenderService.sendEmail(email, username);
        return true;
    }

}

