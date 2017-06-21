package ar.org.blb.login.administration.responses;

import ar.org.blb.login.administration.entities.Authentication;

public class AuthenticationResponse {

    private HttpStatusResponse status;
    private Authentication entity;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(HttpStatusResponse status, Authentication entity) {
        this.status = status;
        this.entity = entity;
    }

    public HttpStatusResponse getStatus() {
        return status;
    }

    public void setStatus(HttpStatusResponse status) {
        this.status = status;
    }

    public Authentication getEntity() {
        return entity;
    }

    public void setEntity(Authentication entity) {
        this.entity = entity;
    }
}
