package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

public class User {
    private final UserId userId;
    private final String email;
    private final String firstName;

    public User(UserId userId, String email, String firstName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
    }

    public UserId id() {
        return userId;
    }

    public String email() {
        return email;
    }

    public String firstName() {
        return firstName;
    }
}
