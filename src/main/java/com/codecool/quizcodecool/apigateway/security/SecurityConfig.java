package com.codecool.quizcodecool.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll() // allowed by anyone
                .antMatchers(HttpMethod.GET, "/categories").authenticated() // allowed only when signed in
                .antMatchers("/customquizzes/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.GET, "/questions/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.POST, "/questions/add").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.PUT, "/questions/**").hasAuthority("ROLE_ADMIN") // allowed only when signed in
                .antMatchers(HttpMethod.DELETE, "/questions/**").hasAuthority("ROLE_ADMIN") // allowed only when signed in
                .antMatchers("/types/**").authenticated() // allowed only when signed in
                .anyRequest().denyAll() // anything else is denied
                // NEW PART:
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class)
                .cors();
    }

}
