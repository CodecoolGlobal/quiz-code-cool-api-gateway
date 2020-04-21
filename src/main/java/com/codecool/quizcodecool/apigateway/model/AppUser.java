package com.codecool.quizcodecool.apigateway.model;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String password;

    private String email;

    private LocalDate registrationDate;

    @ElementCollection
    @Singular
    private List<String> roles;

}
