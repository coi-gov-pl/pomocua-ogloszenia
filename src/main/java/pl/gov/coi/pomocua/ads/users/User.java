package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

public class User {
    private final UserId userId;
    private final String email;
    private final String phoneNumber;

    public User() {
        this(null,null,null);
    }

    public User(UserId userId, String email) {
        this(userId, email, null);
    }
    public User(UserId userId, String email, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserId id() {
        return userId;
    }

    public String email() {
        return email;
    }

    public String phoneNumber() {
        return phoneNumber;
    }
}
