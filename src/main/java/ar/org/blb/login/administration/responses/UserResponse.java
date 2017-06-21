package ar.org.blb.login.administration.responses;

import ar.org.blb.login.administration.entities.User;

public class UserResponse {

    private HttpStatusResponse status;
    private User entity;

    public UserResponse() {
    }

    public UserResponse(HttpStatusResponse status, User entity) {
        this.status = status;
        this.entity = entity;
    }

    public HttpStatusResponse getStatus() {
        return status;
    }

    public void setStatus(HttpStatusResponse status) {
        this.status = status;
    }

    public User getEntity() {
        return entity;
    }

    public void setEntity(User entity) {
        this.entity = entity;
    }
}
