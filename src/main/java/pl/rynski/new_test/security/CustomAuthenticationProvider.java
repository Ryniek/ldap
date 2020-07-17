package pl.rynski.new_test.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.rynski.new_test.client.LdapClient;

import javax.naming.directory.DirContext;
import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LdapClient ldapClient;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try{
            if(ldapClient.authenticate(username, password) == null) {
                throw new BadCredentialsException("Unable to authenticate user: " + username);
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Unable to authenticate user: " + username);
        }
        User user = new User(username, password, Collections.singleton(null));
        return new UsernamePasswordAuthenticationToken(user, password, Collections.singleton(null));
    }
}
