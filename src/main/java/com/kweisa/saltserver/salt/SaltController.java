package com.kweisa.saltserver.salt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@RestController("/salt")
public class SaltController {
    private SaltRepository saltRepository;

    @Autowired
    public SaltController(SaltRepository saltRepository) {
        this.saltRepository = saltRepository;
    }

    private String sha3(String message) throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA3-256").digest(message.getBytes()));
    }

    @Transactional
    @PostMapping
    public ResponseEntity createUser(@RequestParam String username, @RequestParam String password, @RequestParam String salt) throws NoSuchAlgorithmException {
        String hashedPassword = sha3(password);

        Optional<SaltModel> optionalSaltModel = saltRepository.findById(username);
        if (optionalSaltModel.isPresent()) {
            SaltModel saltModel = optionalSaltModel.get();
            saltModel.setPassword(hashedPassword);
            saltModel.setSalt(salt);
            System.out.println(username + hashedPassword + saltModel.getSalt());
            return new ResponseEntity(HttpStatus.OK);
        }

        SaltModel saltModel = new SaltModel(username, hashedPassword, salt);
        System.out.println(username + hashedPassword + salt);
        saltRepository.save(saltModel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> readSalt(@RequestParam String username, @RequestParam String password) throws NoSuchAlgorithmException {
        Optional<SaltModel> optionalSaltModel = saltRepository.findById(username);
        if (optionalSaltModel.isPresent()) {
            SaltModel saltModel = optionalSaltModel.get();
            if (saltModel.getPassword().equals(sha3(password))) {
                System.out.println(username + password + saltModel.getSalt());
                return new ResponseEntity<>(saltModel.getSalt(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
