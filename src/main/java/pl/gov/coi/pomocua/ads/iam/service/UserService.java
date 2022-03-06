package pl.gov.coi.pomocua.ads.iam.service;

import pl.gov.coi.pomocua.ads.UserId;
import pl.gov.coi.pomocua.ads.iam.UserDetails;

public interface UserService {

    UserDetails getUserDetails(UserId userId);
}
