package pl.gov.coi.pomocua.ads.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.authentication.UnauthorizedException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersResource {

    private final CurrentUser currentUser;
    private final UsersRepository usersRepository;

    @GetMapping("secure/me")
    public ResponseEntity<UserInfo> me() {
        return ResponseEntity.ok(UserInfo.from(usersRepository.getById(currentUser.getCurrentUserId())).orElseThrow(UnauthorizedException::new));
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
    public static final class UserInfo {
        public String email;
        public String phoneNumber;

        public UserInfo() {
        }

        public UserInfo(String email, String phoneNumber) {
            this.email = email;
            this.phoneNumber = phoneNumber;
        }

        public static Optional<UserInfo> from(Optional<User> user) {
            return user.map(value -> new UserInfo(value.email(), value.phoneNumber()));
        }
    }
}
