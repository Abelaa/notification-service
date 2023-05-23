package com.sa.notification;

public class EmailInfo {
    private String email;
    private String message;

    public EmailInfo() {}

    public EmailInfo(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailInfo{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
