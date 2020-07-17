package pl.rynski.new_test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(
    base = "ou=users",
    objectClasses = { "person", "organizationalPerson", "inetOrgPerson", "top" })
public final class User {
    @Id
    @JsonIgnore
    private Name id;

    private @Attribute(name = "cn") String username;
    private @Attribute(name = "userPassword") String password;

    public User() {
    }

    public User(Name id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
