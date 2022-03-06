package pl.gov.coi.pomocua.ads.iam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.iam.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;
    private final CurrentUser currentUser;

    @GetMapping("user-details/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails getUserDetails(@PathVariable("userId") String userId) {
        return userService.getUserDetails(new UserId(userId));
    }

    @GetMapping("secure/user-details")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails getUserDetails() {
        UserId userId = currentUser.getCurrentUserId();
        return userService.getUserDetails(userId);
    }
}
