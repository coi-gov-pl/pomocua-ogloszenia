package pl.gov.coi.pomocua.ads.jobs;

import pl.gov.coi.pomocua.ads.Location;

import java.util.List;

public class JobsTestDataGenerator {
    public static JobOffer sampleOffer() {
        JobOffer request = new JobOffer();
        request.title = "sample work";
        request.mode = JobOffer.Mode.REMOTE;
        request.location = new Location("Mazowieckie", "Warszawa");
        request.type = List.of(JobOffer.Type.TEMPORARY);
        request.language = List.of(JobOffer.Language.PL, JobOffer.Language.UA);
        request.description = "description";
        return request;
    }
}
