package ar.org.blb.login.administration.utilities;

import ar.org.blb.login.administration.entities.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class LoginUtility {

    @Value("${login.activation.expiration.date.amount}")
    private String amount;

    @Value("${login.authentication.token.validity.time}")
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

    public String getCodeRoleCommaSeparated(List<Role> roles) {
        return roles.stream()
                .map(Role::getCode)
                .collect(Collectors.joining(","));
    }
}
