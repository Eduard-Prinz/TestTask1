package com.gridnine.testing.model;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Flight {
    private final List<Segment> segments;

    public Flight(final List<Segment> segs) {
        segments = segs;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public long getDurationInHours() {
        return segments.stream()
                .mapToLong(segment -> Duration.between(segment.getDepartureDate(), segment.getArrivalDate()).toHours())
                .sum();
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}
