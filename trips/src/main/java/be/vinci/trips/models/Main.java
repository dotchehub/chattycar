package be.vinci.trips.models;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Main {

  static List<Passenger>  iterable = new ArrayList<>();
  static List<Trips> trips = new ArrayList<>();

  private static Trips getOne(long id) {
    return trips.get((int) id);
  }

  public static void main(String[] args) {
    Trips t1 = new Trips();
    t1.setDeparture(LocalDate.now().plusMonths(5));
    t1.setIdTrip(0);
    Passenger p1Trp0 = new Passenger();
    p1Trp0.setIdPassenger(0);
    p1Trp0.setIdTrips(0);
    p1Trp0.setStatus("pending");
    iterable.add(p1Trp0);
    trips.add(t1);

    Trips t2 = new Trips();
    t2.setDeparture(LocalDate.now().plusMonths(5));
    t2.setIdTrip(1);
    Passenger p1Trp1 = new Passenger();
    p1Trp1.setIdPassenger(1);
    p1Trp1.setIdTrips(1);
    p1Trp1.setStatus("accepted");
    iterable.add(p1Trp1);
    trips.add(t2);

    Trips t3 = new Trips();
    t3.setDeparture(LocalDate.now().minusMonths(5));
    t3.setIdTrip(1);
    Passenger p1Trp2 = new Passenger();
    p1Trp2.setIdPassenger(2);
    p1Trp2.setIdTrips(2);
    p1Trp2.setStatus("pending");
    iterable.add(p1Trp2);
    trips.add(t3);

    var map =
        StreamSupport.stream(iterable.spliterator(), false)
            .filter(d -> getOne(d.getIdTrips()).getDeparture().isAfter(LocalDate.now())).
        collect(Collectors.groupingBy(Passenger::getStatus,
            Collectors.mapping(d -> getOne(d.getIdTrips()), Collectors.toList())));

    for (var entry : map.entrySet()) {
      System.out.println(entry.getKey() + "/" + entry.getValue());
    }
  }
}
