package org.woehlke.simpleworklist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.woehlke.simpleworklist.application.LoginSuccessHandler;
import org.woehlke.simpleworklist.services.UserAccountSecurityService;

@Configuration
@EnableWebSecurity
@EnableSpringDataWebSupport
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers().disable()
            .authorizeRequests()
            .antMatchers(
                    "/webjars/**", "/css/**", "/img/**", "/js/**", "/favicon.ico",
                    "/test*/**", "/login*", "/register*", "/confirm*/**",
                    "/resetPassword*", "/passwordResetConfirm*/**"
            )
            .permitAll()
            .anyRequest().authenticated().antMatchers("/**").fullyAuthenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .usernameParameter("j_username").passwordParameter("j_password")
            .loginProcessingUrl("/j_spring_security_check")
            .failureForwardUrl("/login?login_error=1")
            .defaultSuccessUrl("/")
            .successHandler(loginSuccessHandler)
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll();
    }

    @Bean
    public PasswordEncoder encoder(){
        // @see https://www.dailycred.com/article/bcrypt-calculator
        int strength = applicationProperties.getUser().getStrengthBCryptPasswordEncoder();
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return auth.userDetailsService(userAccountSecurityService).passwordEncoder(encoder()).and().build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/j_spring_security_check");
        return filter;
    }

    @Autowired
    private AuthenticationManagerBuilder auth;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserAccountSecurityService userAccountSecurityService;

}
