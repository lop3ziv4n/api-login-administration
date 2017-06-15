package ar.org.blb.login.administration.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class LoginUtility {

    @Value(value = "login.activation.expiration.date.amount")
    private String amount;

    @Value(value = "login.authentication.token.validity.time")
    private String tokenValidity;

    public Date getExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, Integer.parseInt(amount));
        return calendar.getTime();
    }

    public String getRandomValue() {
        return UUID.randomUUID().toString();
    }

    public Integer getTokenValidity() {
        return Integer.parseInt(tokenValidity);
    }
}
