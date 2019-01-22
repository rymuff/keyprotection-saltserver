package com.kweisa.saltserver.salt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity createUser(@RequestParam String id, @RequestParam String password, @RequestParam String salt) throws NoSuchAlgorithmException {
        Optional<SaltModel> optionalSaltModel = saltRepository.findById(id);
        if (optionalSaltModel.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = sha3(password);
        SaltModel saltModel = new SaltModel(id, hashedPassword, salt);
        saltRepository.save(saltModel);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<String> readSalt(@RequestParam String id, @RequestParam String password) throws NoSuchAlgorithmException {
        Optional<SaltModel> optionalSaltModel = saltRepository.findById(id);
        if (optionalSaltModel.isPresent()) {
            SaltModel saltModel = optionalSaltModel.get();
            if (saltModel.getPassword().equals(sha3(password))) {
                return new ResponseEntity<>(saltModel.getSalt(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
