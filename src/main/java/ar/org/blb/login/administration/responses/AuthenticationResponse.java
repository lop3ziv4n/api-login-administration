package ar.org.blb.login.administration.responses;

import ar.org.blb.login.administration.entities.Authentication;
import ar.org.blb.login.administration.utilities.AuthenticationStatus;

public class AuthenticationResponse {

    private StatusResponse status;
    private Authentication entity;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(StatusResponse status, Authentication entity) {
        this.status = status;
        this.entity = entity;
    }

    public AuthenticationResponse(AuthenticationStatus status, Authentication entity) {
        this.status = new StatusResponse(status);
        this.entity = entity;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public Authentication getEntity() {
        return entity;
    }

    public void setEntity(Authentication entity) {
        this.entity = entity;
    }
}
