package com.example.application.security;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    public static final String LOGOUT_URL = "/";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);
        //1.
        // Causes blank page with error message in browser console about unsafe-inline
        //http.headers().contentSecurityPolicy("script-src 'self'");

        //2.
        //Causes an eval to fail...
        //http.headers().contentSecurityPolicy("script-src 'unsafe-inline' 'self'");

        //3.
        //App working again at least on dev machine...
        http.headers().contentSecurityPolicy("script-src 'unsafe-inline' 'unsafe-eval' 'self'");

        /*
         *TODO, style-src 'unsafe-inline' is also mentioned in the FAQ, however did not seem to be needed in the hello world app.
         * Multiple CSP rules can be given at the same time separated by ; as described for instance here:
         * https://www.baeldung.com/spring-security-csp
         */
        setLoginView(http, LoginView.class, LOGOUT_URL);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/*.png");
    }
}

