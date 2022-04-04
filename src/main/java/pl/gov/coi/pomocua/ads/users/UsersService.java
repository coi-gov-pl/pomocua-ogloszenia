package pl.gov.coi.pomocua.ads.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.authentication.CurrentUser;
import pl.gov.coi.pomocua.ads.myoffers.MyOffersRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final CurrentUser currentUser;
    private final UsersRepository usersRepository;
    private final MyOffersRepository myOffersRepository;

    public User getCurrentUser() {
        UserId currentUserId = currentUser.getCurrentUserId();
        return usersRepository.getById(currentUserId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void removeCurrentUser() {
        UserId userId = currentUser.getCurrentUserId();
        myOffersRepository.setUserOffersAsInactive(userId);
        usersRepository.removeUser(userId);
    }
}
