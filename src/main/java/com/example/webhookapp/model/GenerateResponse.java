package com.example.webhookapp.model;

public class GenerateResponse {

    private String webhook;
    private String accessToken;

    // Getter for webhook
    public String getWebhook() {
        return webhook;
    }

    // Setter for webhook
    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    // Getter for accessToken
    public String getAccessToken() {
        return accessToken;
    }

    // Setter for accessToken
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}