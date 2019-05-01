package org.woehlke.simpleworklist.model.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.oodm.entities.UserAccount;
import org.woehlke.simpleworklist.model.beans.UserDetailsBean;
import org.woehlke.simpleworklist.oodm.repository.UserAccountRepository;
import org.woehlke.simpleworklist.model.services.UserAccountSecurityPasswordService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserAccountSecurityPasswordServiceImpl implements UserAccountSecurityPasswordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAccountSecurityPasswordServiceImpl.class);

    private final UserAccountRepository userAccountRepository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserAccountSecurityPasswordServiceImpl(UserAccountRepository userAccountRepository, AuthenticationManager authenticationManager) {
        this.userAccountRepository = userAccountRepository;
        int strength = 10;
        this.encoder = new BCryptPasswordEncoder(strength);
        this.authenticationManager = authenticationManager;
    }


    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(token);
        if (authenticationResult.isAuthenticated()) {
            UserAccount ua = userAccountRepository.findByUserEmail(user.getUsername());
            String pwEncoded = encoder.encode(newPassword);
            ua.setUserPassword(pwEncoded);
            userAccountRepository.saveAndFlush(ua);
            return new UserDetailsBean(ua);
        } else {
            return user;
        }
    }
}
