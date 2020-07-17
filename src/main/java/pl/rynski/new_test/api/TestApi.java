package pl.rynski.new_test.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.rynski.new_test.client.LdapClient;
import pl.rynski.new_test.model.User;
import pl.rynski.new_test.model.UserModel;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class TestApi {
    @Autowired
    private LdapClient ldapClient;

    @GetMapping("/login")
    public String getHome() {
        return "U are logged in";
    }

    @GetMapping("/user/{name}")
    public List<String> getUser(@PathVariable String name) {
        return ldapClient.search(name);
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody UserModel userModel) throws NoSuchAlgorithmException {
        ldapClient.createUser(userModel.getUsername(), userModel.getPassword());
        ldapClient.addRole(userModel.getUsername(), "users");
    }

    @PostMapping("/addAdmin")
    public void addAdmin(@RequestBody UserModel userModel) throws NoSuchAlgorithmException {
        ldapClient.createUser(userModel.getUsername(), userModel.getPassword());
        ldapClient.addRole(userModel.getUsername(), "admins");
    }

    @GetMapping("/users")
    public List<User> getAll() throws NoSuchAlgorithmException {
        return ldapClient.findAll();
    }
}
