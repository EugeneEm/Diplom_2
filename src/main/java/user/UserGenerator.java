package user;

import java.time.LocalDateTime;

    public class UserGenerator {

    public User generic() {
        return new User("Anakin", "thechoosen@starwars.com", "p@ssword123");
    }

    public User random() {
        return new User("Luke" + LocalDateTime.now(), "skywalker" + LocalDateTime.now() + "@starwars.com", "p@sSword" + LocalDateTime.now());
    }
}
