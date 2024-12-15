package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    @Autowired
    CredentialMapper credentialMapper;

    public Credential[] getCredentialsByUser(Integer userid) {
        return credentialMapper.getCredential(userid);
    }

    public void insert(Credential credential) {
        credentialMapper.insert(credential);
    }

    public void update(Credential credential) {
        credentialMapper.updateCredential(
                credential.getCredentialid(),
                credential.getUsername(),
                credential.getUrl(),
                credential.getKey(),
                credential.getPassword()
        );
    }

    public void delete(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }
}
