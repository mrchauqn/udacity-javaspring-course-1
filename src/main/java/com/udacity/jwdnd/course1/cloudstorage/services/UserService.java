package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HashService hashService;

    @Autowired
    private AuthenticationService authenticationService;

    public boolean isExistUsername(String username) {
        return userMapper.getUser(username.toLowerCase()) != null;
    }

    public Integer createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String encodeSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodeSalt);

        return userMapper.createUser(new User(null, user.getUsername(), encodeSalt, hashedPassword, user.getFirstname().toLowerCase(), user.getLastname().toLowerCase()));
    }

    public Integer getUserId(String username) {
        User user = userMapper.getUser(username);
        return user.getUserid();
    }
}
