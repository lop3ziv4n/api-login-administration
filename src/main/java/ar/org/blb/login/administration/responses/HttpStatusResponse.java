package ar.org.blb.login.administration.responses;

public enum HttpStatusResponse {

    USER_NOT_ACTIVE(800, "User Not Active"),
    USER_NOT_EXISTS(801, "User Not Exists"),
    USERNAME_NOT_EXISTS(802, "Username Not Exists"),
    PASSWORD_INCORRECT(803, "Password Incorrect"),
    AUTHENTICATION_FAILED(804, "Authentication Failed"),
    AUTHENTICATION_OK(805, "Authentication Ok"),
    USERNAME_EXISTS(806, "Username Exists"),
    USER_CREATED_FAILED(807, "User Created Failed"),
    USER_CREATED(808, "User Created"),
    ACTIVATION_NOTIFICATION(809, "Activation Notification"),
    ACTIVATION_NOTIFICATION_FAILED(810, "Activation Notification Failed"),
    RESET_PASSWORD_NOTIFICATION(811, "Reset Password Notification"),
    RESET_PASSWORD_NOTIFICATION_FAILED(812, "Reset Password Notification Failed"),
    PASSWORD_CHANGED(813, "Password changed"),
    PASSWORD_CHANGED_FAILED(814, "Password changed Failed"),
    EMAIL_NOT_EXISTS(815, "Email Not Exists"),
    RESET_PASSWORD_KEY_NOT_EXISTS(816, "Reset Password Key Not Exists"),
    ACTIVATION_FAILED(817, "Activation Failed"),
    ACTIVATION_OK(818, "Activation Ok");

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
