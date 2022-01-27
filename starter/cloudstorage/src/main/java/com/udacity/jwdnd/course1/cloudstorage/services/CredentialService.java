package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void addCredential (CredentialForm credentialForm) {
        // Create random key
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        // Encrypt given password using random key
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getCredentialPassword(), encodedKey);

        // Set encrypted password and key
        credentialForm.setCredentialPassword(encryptedPassword);
        credentialForm.setKey(encodedKey);

        Credential newCredential = getCredentialObj(credentialForm);

        credentialMapper.addCredential(newCredential);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public Credential[] getCredentialListByUserId(Integer userId) {
        Credential[] credentialArr = credentialMapper.getCredentialsByUserId(userId);
        String decryptPassword;

        // Decrypt all credential stored passwords
        for(Credential credential : credentialArr) {
            decryptPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
            credential.setPassword(decryptPassword);
        }

        return credentialArr;
    }

    public void editCredential (CredentialForm credentialForm) {
        Credential editCredential = getCredentialObj(credentialForm);

        credentialMapper.editCredential(editCredential);
    }
    private Credential getCredentialObj(CredentialForm credentialForm) {
        Credential newCredential = new Credential(credentialForm.getCredentialId(), credentialForm.getCredentialURL(), credentialForm.getCredentialUsername(),
                credentialForm.getKey(), credentialForm.getCredentialPassword(), credentialForm.getUserId());

        return newCredential;
    }
}
