package com.codecool.quizcodecool.apigateway.repository;

import com.codecool.quizcodecool.apigateway.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findByUsername(String name);
    Optional<AppUser> findByEmail(String email);


}
