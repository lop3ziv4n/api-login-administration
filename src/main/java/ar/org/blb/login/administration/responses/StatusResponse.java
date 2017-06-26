package ar.org.blb.login.administration.responses;

import ar.org.blb.login.administration.utilities.AuthenticationStatus;

public class StatusResponse {

    private Integer code;
    private String description;

    public StatusResponse() {
    }

    public StatusResponse(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public StatusResponse(AuthenticationStatus status) {
        this.code = status.value();
        this.description = status.getReasonPhrase();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
