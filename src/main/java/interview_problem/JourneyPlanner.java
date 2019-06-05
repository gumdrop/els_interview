package interview_problem;

import com.google.common.collect.Streams;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JourneyPlanner {

    final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("kkmm");

    final List<List<Entry>> timetable;

    public JourneyPlanner(final String[][] timetable) {


        this.timetable = Arrays.stream(timetable).skip(1).reduce(new ArrayList<>(), (rows, row) -> {

            final Stream<String> stations = Arrays.stream(timetable[0]);

            rows.add(
                    Streams
                            .zip(
                                    stations,
                                    Arrays.stream(row),
                                    (station, time) -> new Entry(
                                            station,
                                            LocalTime.parse(time, timeFormatter)))
                            .collect(Collectors.toList()));


            return rows;

        }, (a, b) -> a);
    }

    public long duration(String from, String to, LocalTime arrive, boolean includeWaiting) {
        Optional<List<Entry>> found = timetable
                .stream()
                .filter(row ->
                        row
                            .stream()
                            .anyMatch(entry ->
                                    entry.station.equals(from) && entry.time.compareTo(arrive) >= 0)
                ).findFirst();

        return found.map(row -> {

            LocalTime start = includeWaiting
                    ? arrive
                    : row
                        .stream()
                        .filter(entry -> entry
                                .station
                                .equals((from)))
                        .map(entry -> entry.time)
                        .findFirst().get();

            LocalTime end = row
                    .stream()
                    .filter(entry -> entry.station.equals(to))
                    .findFirst()
                    .map(entry -> entry.time)
                    .orElseThrow(() -> new IllegalArgumentException("End station not found"));

            return start.until(end, ChronoUnit.MINUTES);

        }).orElseThrow(() -> new IllegalArgumentException("Invalid journey"));
    }
}

class Entry {
    final String station;
    final LocalTime time;

    Entry(String station, LocalTime time) {
        this.station = station;
        this.time = time;
    }
}
