package org.kalla.enterprise.movil.gps.vo;

public class ResponseVO {

    private String message;
    private String success;

    public ResponseVO() {
    }

    public ResponseVO(String message, String success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
