package pl.gov.coi.pomocua.ads;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class TercToSql {

    List<String> tercToSql(Path tercFile) {
        List<TercLine> tercLines = extractTercLines(tercFile).toList();
        Map<String, String> voivodeships = extractVoivodeships(tercLines);
        Set<VoivodeshipAndCity> rows = extractCities(tercLines, voivodeships);
        return convertToSql(rows);
    }

    private Stream<TercLine> extractTercLines(Path file) {
        try {
            return Files
                    .lines(file)
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .map(line -> {
                        String[] parts = line.split(";");
                        return new TercLine(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed while reading lines from " + file);
        }
    }

    private Map<String, String> extractVoivodeships(List<TercLine> terc) {
        return terc.stream().filter(t -> t.type2.equals("województwo")).collect(toMap(x -> x.voivodeshipId, x -> x.name.toLowerCase()));
    }

    private Set<VoivodeshipAndCity> extractCities(List<TercLine> terc, Map<String, String> voivodeships) {
        return terc.stream()
                .filter(t -> !t.type2.equals("województwo"))
                .filter(t -> !t.type2.equals("powiat"))
                .map(t -> new VoivodeshipAndCity(voivodeships.get(t.voivodeshipId), t.name.toLowerCase()))
                .collect(toSet());
    }

    private List<String> convertToSql(Set<VoivodeshipAndCity> vacs) {
        AtomicInteger id = new AtomicInteger(0);
        return vacs.stream().map(vac -> vacToSql(id.getAndIncrement(), vac)).toList();
    }

    private String vacToSql(long id, VoivodeshipAndCity vac) {
        return String.format("INSERT INTO city(id, region, city) VALUES(%d, '%s', '%s');",id, vac.voivodeship, vac.city);
    }

    public static void main(String[] args) {
        new TercToSql()
                .tercToSql(Paths.get(args[0]))
                .forEach(System.out::println);
    }

    record TercLine(String voivodeshipId, String district, String community, String type, String name, String type2) {
    }

    record VoivodeshipAndCity(String voivodeship, String city) {
    }
}
