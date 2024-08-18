import com.gridnine.testing.builder.FlightBuilder;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import com.gridnine.testing.filter.FlightFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class MainTest {
    private List<Flight> flights;

    @BeforeEach
    public void setUp() {
        flights = FlightBuilder.createFlights();
    }

    @Test
    void createFlightsTest() {
        Assertions.assertNotNull(flights);
        Assertions.assertEquals(6, flights.size());

        Flight firstFlight = flights.get(0);
        Assertions.assertEquals(1, firstFlight.getSegments().size());
        Assertions.assertEquals(2, firstFlight.getDurationInHours());

        Flight secondFlight = flights.get(1);
        Assertions.assertEquals(2, secondFlight.getSegments().size());
        Assertions.assertEquals(4, secondFlight.getDurationInHours());

        Flight thirdFlight = flights.get(2);
        Assertions.assertEquals(1, thirdFlight.getSegments().size());
        Assertions.assertTrue(thirdFlight.getSegments().get(0).getDepartureDate()
                .isBefore(LocalDateTime.now()));

        Flight fourthFlight = flights.get(3);
        Assertions.assertEquals(1, fourthFlight.getSegments().size());
        Segment segment = fourthFlight.getSegments().get(0);
        Assertions.assertTrue(segment.getDepartureDate().isAfter(segment.getArrivalDate()));

        Flight fifthFlight = flights.get(4);
        Assertions.assertEquals(2, fifthFlight.getSegments().size());

        Flight sixthFlight = flights.get(5);
        Assertions.assertEquals(3, sixthFlight.getSegments().size());
    }

    @Test
    void arrivalBeforeDeparturePresentTest() {
        FlightFilter filter = new FlightFilter(flights);
        List<Flight> filteredFlights = filter.arrivalBeforeDeparturePresent().build();
        int validSegmentCount = 0;

        for (Flight flight : filteredFlights) {
            for (Segment segment : flight.getSegments()) {
                Assertions.assertFalse(segment.getDepartureDate().isBefore(LocalDateTime.now()));
                validSegmentCount++;
            }
        }
        int totalSegmentCount = filteredFlights.stream()
                .mapToInt(flight -> flight.getSegments().size())
                .sum();

        Assertions.assertEquals(totalSegmentCount, validSegmentCount);
    }

    @Test
    void ArrivalBeforeDepartureTest() {
        FlightFilter filter = new FlightFilter(flights);
        List<Flight> filteredFlights = filter.filterArrivalBeforeDeparture().build();
        int validSegmentCount = 0;

        for (Flight flight : filteredFlights) {
            for (Segment segment : flight.getSegments()) {
                Assertions.assertFalse(segment.getArrivalDate().isBefore(segment.getDepartureDate()));
                validSegmentCount++;
            }
        }
        int totalSegmentCount = filteredFlights.stream()
                .mapToInt(flight -> flight.getSegments().size())
                .sum();

        Assertions.assertEquals(totalSegmentCount, validSegmentCount);
    }

    @Test
    public void timeMoreTwoHoursTest() {
        FlightFilter filter = new FlightFilter(flights);
        List<Flight> filteredFlights = filter.timeMoreTwoHours().build();

        for (Flight flight : filteredFlights) {
            Duration totalWaitTime = Duration.ZERO;
            List<Segment> segments = flight.getSegments();

            for (int i = 1; i < segments.size(); i++) {
                LocalDateTime currentDeparture = segments.get(i).getDepartureDate();
                LocalDateTime lastArrival = segments.get(i - 1).getArrivalDate();
                Duration waitTime = Duration.between(lastArrival, currentDeparture).abs();

                totalWaitTime = totalWaitTime.plus(waitTime);
            }

            Assertions.assertTrue(totalWaitTime.toHours() < 2 + totalWaitTime.toHours());
        }
    }
}
