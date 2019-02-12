package com.kweisa.saltserver.salt;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SaltModel {
    @Id
    private String username;
    private String password;
    private String salt;

    public SaltModel() {
    }

    public SaltModel(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
