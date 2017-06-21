package ar.org.blb.login.administration.responses;

public enum HttpStatusResponse {

    USER_NOT_ACTIVE(800, "User Not Active"),
    USERNAME_NOT_EXISTS(801, "Username Not Exists"),
    PASSWORD_INCORRECT(802, "Password Incorrect"),
    AUTHENTICATION_FAILED(803, "Authentication Failed"),
    AUTHENTICATION_OK(804, "Authentication Ok"),
    USERNAME_EXISTS(805, "Username Exists"),
    USER_CREATED_FAILED(806, "User Created Failed"),
    USER_CREATED(807, "User Created"),
    ACTIVATION_NOTIFICATION(808, "Activation Notification"),
    ACTIVATION_NOTIFICATION_FAILED(809, "Activation Notification Failed"),
    RESET_PASSWORD_NOTIFICATION(810, "Reset Password Notification"),
    RESET_PASSWORD_NOTIFICATION_FAILED(811, "Reset Password Notification Failed"),
    PASSWORD_CHANGED(810, "Password changed"),
    PASSWORD_CHANGED_FAILED(811, "Password changed Failed");

    private final int value;
    private final String reasonPhrase;

    HttpStatusResponse(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public static HttpStatusResponse valueOf(int statusCode) {
        for (HttpStatusResponse status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }
}
