package ar.org.blb.login.administration.services;

import ar.org.blb.login.administration.entities.Authentication;
import ar.org.blb.login.administration.repositories.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private AuthenticationRepository authenticationRepository;

    @Autowired
    public AuthenticationService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public Authentication findAuthenticationByToken(String token) {
        return this.authenticationRepository.findOneByToken(token);
    }

    public Authentication findAuthenticationByUser(Long user) {
        return this.authenticationRepository.findOneByUser(user);
    }

    public Authentication createAuthentication(Authentication authentication) {
        return this.authenticationRepository.save(authentication);
    }

    public void deleteAuthentication(Long id) {
        Optional.ofNullable(this.authenticationRepository.findOne(id))
                .ifPresent(u -> this.authenticationRepository.delete(u));
    }

    public Authentication updateAuthentication(Authentication authentication, Long id) {
        return Optional.ofNullable(this.authenticationRepository.findOne(id))
                .map(u -> {
                    u.setToken(authentication.getToken());
                    u.setAuthorities(authentication.getAuthorities());
                    u.setTokenValidity(authentication.getTokenValidity());
                    u.setUser(authentication.getUser());
                    return this.authenticationRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("No Exists Authentication"));
    }
}