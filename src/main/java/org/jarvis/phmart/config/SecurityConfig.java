package org.jarvis.phmart.config;

import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.sercurity.service.AuthenticationService;
import org.jarvis.sercurity.service.impl.DefaultAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created: kim chheng
 * Date: 18-May-2019 Sat
 * Time: 10:02 AM
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticationService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .regexMatchers("/frontend/.*", "/VAADIN/.*", "/login.*", "/accessDenied.*").permitAll()
                .regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
                .antMatchers("/product/**", "/report/**").hasRole("ADMIN")
                .antMatchers("/sale/**").hasAnyRole("ADMIN", "USER")
                //.requestMatchers(this::isFrameworkInternalRequest).permitAll()
                //.antMatchers(HttpMethod.POST, ALLOWED_URLS).permitAll()
                //.antMatchers(HttpMethod.GET, ALLOWED_URLS).permitAll()
                //.anyRequest().authenticated()
                .antMatchers("/**").fullyAuthenticated()
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/sale", true)
                .failureUrl("/login?error")
                .and().exceptionHandling().accessDeniedPage("/accessDenied")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
        //.and().sessionManagement().sessionAuthenticationStrategy()
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/manifest.json",
                "/sw.js",
                "/offline-page.html",
                "/offline.html",
                "/frontend/**",
                "/backend/**",
                "/webjars/**",
                "/frontend-es5/**",
                "/frontend-es6/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationService authenticationService(EntityRepository entityRepository) {
        return new DefaultAuthenticationService(entityRepository);
    }

    private static final String[] ALLOWED_URLS = {
            "/VAADIN/**",
            "/HEARTBEAT/**",
            "/PUSH/**",
            "/UIDL/**",
            "/login/**",
            "/logout/**",
            "/error/**",
            "/accessDenied/**",
            "/vaadinServlet/**",
            "/favicon.ico",
            "/robots.txt",
            "/sw.js",
            "/manifest.webmanifest",
            "/manifest.json",
            "/service-worker.js",
            "/offline-page.html",
            "/frontend/**",
            "/webjars/**",
            "/frontend-es5/**",
            "/frontend-es6/**"
    };
}
