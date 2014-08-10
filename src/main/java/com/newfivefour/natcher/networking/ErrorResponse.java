package com.newfivefour.natcher.networking;

/**
 * Extended to send custom errors down the event bus per request type.
 */
public class ErrorResponse {
    public int responseCode;
    public String responseMessage;
    public String url;
    public boolean isNetworkError;

    public void fill(int httpCode, String errorMessage, String url, boolean isNetworkError) {
        this.responseCode = httpCode;
        this.responseMessage = errorMessage;
        this.url = url;
        this.isNetworkError = isNetworkError;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}