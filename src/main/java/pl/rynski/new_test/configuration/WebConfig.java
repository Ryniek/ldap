package pl.rynski.new_test.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.rynski.new_test.security.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/users").hasRole("ADMINS")
            .antMatchers("/user/*").hasRole("USERS")
            .antMatchers("/addUser", "/addAdmin").hasRole("ADMINS")
            .anyRequest().fullyAuthenticated()
            .and()
            .httpBasic()
            .and()
            .csrf()
            .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(authenticationProvider)
            .ldapAuthentication()
            .userDnPatterns("cn={0},ou=users")
            .userSearchBase("ou=users")
            .userSearchFilter("cn={0}")
            .groupSearchBase("ou=groups")
            .groupSearchFilter("uniqueMember={0}")
            .contextSource()
            .url("ldap://localhost:18889/dc=example,dc=com")
            .and()
            .passwordCompare()
            .passwordEncoder(getPasswordEncoder())
            .passwordAttribute("userPassword");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
