package pl.gov.coi.pomocua.ads.users;

import pl.gov.coi.pomocua.ads.UserId;

public class User {
    private final UserId userId;
    private final String email;
    private final String firstName;
    private final String phoneNumber;

    public User(UserId userId, String email, String firstName) {
        this(userId, email, firstName, null);
    }

    public User(UserId userId, String email, String firstName, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
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

    public String firstName() {
        return firstName;
    }
}
