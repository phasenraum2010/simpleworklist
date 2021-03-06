package org.woehlke.simpleworklist.user.domain.account;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.woehlke.simpleworklist.application.config.AbstractTest;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserAccountPasswordEncodedTest extends AbstractTest {

    @Autowired
    private PasswordEncoder encoder;

    //@Test
    public void testEncoderIsWired(){
        assertTrue(encoder != null);
    }

    /**
     * This Test is obsolete now due to changed encoder from MD5 to BCrypt (20.02.2016).
     */
    //@Test
    public void testGetUserPasswordEncoded(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01//@Test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        String encodedPassword =  encoder.encode(u.getUserPassword());
        assertTrue(encodedPassword.compareTo(encoder.encode(u.getUserPassword()))==0);
    }

    //@Test
    public void testPasswordsAreTheSame(){
        UserAccountForm u = new UserAccountForm();
        u.setUserEmail("test01//@Test.de");
        u.setUserFullname("some_name");
        u.setUserPassword("pwd01_ASDFGHJKLMOP_22");
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_22");
        assertTrue(u.passwordsAreTheSame());
        u.setUserPasswordConfirmation("pwd01_ASDFGHJKLMOP_23");
        assertFalse(u.passwordsAreTheSame());
    }
}
