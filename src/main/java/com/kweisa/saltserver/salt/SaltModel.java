package com.kweisa.saltserver.salt;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SaltModel {
    @Id
    private String id;
    private String password;
    private String salt;

    public SaltModel() {
    }

    public SaltModel(String id, String password, String salt) {
        this.id = id;
        this.password = password;
        this.salt = salt;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
