package com.bocom.bocomManager.dto;


public class ResponseMessage {
    private String message;
    private String url;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public ResponseMessage(String message, String url) {
        this.message = message;
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
