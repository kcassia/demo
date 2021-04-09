package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Document
public class User {

    @Id
    private String userId;
    private String userName;
    private Date creationDate = new Date();
    private Map<String, String> userSettings = new HashMap<>();

    public String getUserId() { return userId; }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() { return userName; }
    public void setName(String userName) { this.userName = userName; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Map<String, String> getUserSettings() { return userSettings; }
    public void setUserSettings(Map<String, String> userSettings) { this.userSettings = userSettings; }
}