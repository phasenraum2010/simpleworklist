package org.woehlke.simpleworklist.user.services;

import org.springframework.stereotype.Service;
import org.woehlke.simpleworklist.user.services.TokenGeneratorService;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

    private final SecureRandom random = new SecureRandom();

    public String getToken() {
        int base = 130;
        int strLength = 30;
        return new BigInteger(base, random).toString(strLength) + UUID.randomUUID().toString();
    }
}
