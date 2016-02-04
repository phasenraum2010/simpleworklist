package org.woehlke.simpleworklist.services.impl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.RegistrationProcessStatus;
import org.woehlke.simpleworklist.entities.RegistrationProcessType;
import org.woehlke.simpleworklist.repository.RegistrationProcessRepository;
import org.woehlke.simpleworklist.services.RegistrationProcessService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class RegistrationProcessServiceImpl implements
        RegistrationProcessService {

    @Value("${worklist.registration.max.retries}")
    private int maxRetries;

    @Value("${worklist.registration.ttl.email.verifcation.request}")
    private long ttlEmailVerificationRequest;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationProcessServiceImpl.class);

    @Inject
    private RegistrationProcessRepository registrationProcessRepository;

    @Inject
    private PollableChannel registrationProcessEmailSenderChannel;

    @Inject
    private PollableChannel passwordResetEmailSenderChannel;

    private SecureRandom random = new SecureRandom();

    @Override
    public boolean registrationIsRetryAndMaximumNumberOfRetries(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.REGISTRATION);
        return earlierOptIn == null?false:earlierOptIn.getNumberOfRetries() >= maxRetries;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationCheckIfResponseIsInTime(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.REGISTRATION);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((ttlEmailVerificationRequest + earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()) {
                registrationProcessRepository.delete(earlierOptIn);
            }
        }
    }

    private String getToken() {
        int base = 130;
        int strLength = 30;
        return new BigInteger(base, random).toString(strLength) + UUID.randomUUID().toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSendEmailTo(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.REGISTRATION);
        RegistrationProcess o = new RegistrationProcess();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setRegistrationProcessType(RegistrationProcessType.REGISTRATION);
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = registrationProcessRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        registrationProcessEmailSenderChannel.send(message);
    }

    @Override
    public RegistrationProcess findByToken(String confirmId) {
        return registrationProcessRepository.findByToken(confirmId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySendEmailTo(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.PASSWORD_RECOVERY);
        RegistrationProcess o = new RegistrationProcess();
        if (earlierOptIn != null) {
            o = earlierOptIn;
            o.increaseNumberOfRetries();
        }
        o.setRegistrationProcessType(RegistrationProcessType.PASSWORD_RECOVERY);
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SAVED_EMAIL);
        o.setEmail(email);
        String token = getToken();
        o.setToken(token);
        LOGGER.info("To be saved: " + o.toString());
        o = registrationProcessRepository.saveAndFlush(o);
        LOGGER.info("Saved: " + o.toString());
        Message<RegistrationProcess> message = MessageBuilder.withPayload(o).build();
        passwordResetEmailSenderChannel.send(message);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationSentEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_SENT_MAIL);
        LOGGER.info("about to save: " + o.toString());
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoverySentEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_SENT_EMAIL);
        LOGGER.info("about to save: " + o.toString());
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryClickedInEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_CLICKED_IN_MAIL);
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationClickedInEmail(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_CLICKED_IN_MAIL);
        registrationProcessRepository.saveAndFlush(o);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void registrationUserCreated(RegistrationProcess o) {
        o.setDoubleOptInStatus(RegistrationProcessStatus.REGISTRATION_ACCOUNT_CREATED);
        o = registrationProcessRepository.saveAndFlush(o);
        registrationProcessRepository.delete(o);
    }

    @Override
    public boolean passwordRecoveryIsRetryAndMaximumNumberOfRetries(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.PASSWORD_RECOVERY);
        return earlierOptIn == null?false:earlierOptIn.getNumberOfRetries() >= maxRetries;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryCheckIfResponseIsInTime(String email) {
        RegistrationProcess earlierOptIn = registrationProcessRepository.findByEmailAndRegistrationProcessType(email,RegistrationProcessType.PASSWORD_RECOVERY);
        if (earlierOptIn != null) {
            Date now = new Date();
            if ((ttlEmailVerificationRequest + earlierOptIn.getCreatedTimestamp().getTime()) < now.getTime()) {
                registrationProcessRepository.delete(earlierOptIn);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void passwordRecoveryDone(RegistrationProcess registrationProcess) {
        registrationProcess.setDoubleOptInStatus(RegistrationProcessStatus.PASSWORD_RECOVERY_STORED_CHANGED);
        registrationProcess = registrationProcessRepository.saveAndFlush(registrationProcess);
        registrationProcessRepository.delete(registrationProcess);
    }


}