package com.revworkforce.hrm.dto;

import javax.validation.constraints.NotBlank;

public class NotificationSendRequest {
    @NotBlank
    private String message;
    private Long recipientUserId;
    private boolean sendToAll;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Long recipientUserId) { this.recipientUserId = recipientUserId; }
    public boolean isSendToAll() { return sendToAll; }
    public void setSendToAll(boolean sendToAll) { this.sendToAll = sendToAll; }
}
