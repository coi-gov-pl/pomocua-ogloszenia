package pl.gov.coi.pomocua.ads.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final CurrentUser currentUser;
    private final UsersRepository usersRepository;

    public User getCurrentUser() {
        UserId currentUserId = currentUser.getCurrentUserId();
        return usersRepository.getById(currentUserId)
                .orElseThrow(UserNotFoundException::new);
    }
}
