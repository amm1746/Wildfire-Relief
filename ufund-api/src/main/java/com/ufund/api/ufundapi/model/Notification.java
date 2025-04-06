package com.ufund.api.ufundapi.model;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a notification.
 *
 * @author Alexandra Mantagas
 */

public class Notification {

    private String message;
    private final LocalDateTime timestamp;
    private String sender;
    private List<String> recipients;

    public Notification(String message, String sender, List<String> recipients){
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.sender = sender;
        this.recipients = recipients;

    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

}