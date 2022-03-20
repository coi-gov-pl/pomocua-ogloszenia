package pl.gov.coi.pomocua.ads.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersResource {
    private final UsersService usersService;

    @GetMapping("secure/me")
    public ResponseEntity<UserInfo> me() {
        return ResponseEntity.ok(UserInfo.from(usersService.getCurrentUser()));
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
    public static final class UserInfo {
        public String email;
        public String firstName;
        public String phoneNumber;

        public UserInfo(String email, String firstName, String phoneNumber) {
            this.email = email;
            this.firstName = firstName;
            this.phoneNumber = phoneNumber;
        }

        public static UserInfo from(User user) {
            return new UserInfo(
                user.email(),
                user.firstName(),
                user.phoneNumber()
            );
        }
    }
}
