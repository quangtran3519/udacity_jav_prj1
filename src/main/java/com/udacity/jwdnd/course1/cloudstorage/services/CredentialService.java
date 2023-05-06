package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.ast.Literal;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CredentialService {
    private final Logger logger = LoggerFactory.getLogger(CredentialService.class);

    private final CredentialMapper credentialMapper;

    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        logger.info(" start get Credentials by user id {}", userId);
        List<Credential> credentials = credentialMapper.getCredentials(userId);

        return credentials.stream().filter(credential -> {
            credential.setDecryptedPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
            return true;
        }).collect(Collectors.toList());

    }

    public Credential getCredentialById(Integer id) {
        logger.info(" start get Credential by id {}", id);
        Credential credential = credentialMapper.getCredentialById(id);

        credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));

        return credential;
    }


    public void insertCredential(Credential credential) {
        logger.info(" start insert credential {}", credential);
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        String pwdEncrypted = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setPassword(pwdEncrypted);
        credential.setKey(encodedKey);

        int result = credentialMapper.insertCredential(credential);
        if (result == 0) {
            throw new RuntimeException("insert credential is failed");
        }
    }

    public void deleteCredential(Integer id) {
        logger.info(" start deleteCredential  {}", id);
        int result = credentialMapper.deleteCredential(id);
        if (result == 0) {
            throw new RuntimeException("delete Credential is failed");
        }
    }

    public void updateCredential(Credential credential) {
        logger.info(" start insert Credential {}", credential);

        Credential existCredential = credentialMapper.getCredentialById(credential.getCredentialId());

        if (Objects.isNull(existCredential)) {
            throw new RuntimeException("Credential isn't existed");
        }

        String pwdEncrypted = encryptionService.encryptValue(credential.getPassword(), existCredential.getKey());

        credential.setPassword(pwdEncrypted);
        credential.setKey(existCredential.getKey());


        int result = credentialMapper.updateCredential(credential);
        if (result == 0) {
            throw new RuntimeException("update Credential is failed");
        }
    }


}
