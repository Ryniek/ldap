package pl.rynski.new_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class NewTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewTestApplication.class, args);
        BCryptPasswordEncoder aaa = new BCryptPasswordEncoder();
    }

}
