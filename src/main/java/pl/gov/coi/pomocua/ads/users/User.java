package pl.gov.coi.pomocua.ads.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.gov.coi.pomocua.ads.UserId;

public class User {
    @JsonIgnore
    private final UserId userId;
    public final String email;
    public final String phoneNumber;

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
}
