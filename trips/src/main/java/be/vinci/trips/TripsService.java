package be.vinci.trips;

import be.vinci.trips.data.GpsProxy;
import be.vinci.trips.data.PassengerProxy;
import be.vinci.trips.data.TripsRepository;
import be.vinci.trips.models.NoIdTrips;
import be.vinci.trips.models.Passenger;
import be.vinci.trips.models.Trips;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class TripsService {

  private final TripsRepository repository;
  private final PassengerProxy passengerProxy;
  private final GpsProxy gpsProxy;

  public TripsService(TripsRepository repository, PassengerProxy passengerProxy, GpsProxy gpsProxy
  ) {
    this.repository = repository;
    this.gpsProxy = gpsProxy;
    this.passengerProxy = passengerProxy;
  }

  public Trips createOne(NoIdTrips trips) {
    return repository.save(trips.toTrips());
  }

  public Trips getOne(long id) {
    return repository.findById(id).orElse(null);
  }

  public boolean deleteOne(long id) {
    if (repository.existsById(id)) {
      var trip = repository.findById(id).get();
      repository.delete(trip);
      return true;
    }
    return false;
  }

  public void deleteAllDriverTrips(long driverId) {
    repository.deleteAllByIdDriver(driverId);
  }

  public Map<String, List<Trips>> passengerTripsByStatus(long idPassenger) {
    var listWhereImPassenger = passengerProxy.findTripWhereImPassenger(idPassenger).getBody();
    var map = StreamSupport.stream(listWhereImPassenger.spliterator(), false)
        .filter(d -> getOne(d.getIdTrips()).getDeparture().isAfter(LocalDate.now()))
        .collect(Collectors.groupingBy(
            Passenger::getStatus,
            Collectors.mapping(d -> getOne(d.getIdTrips()), Collectors.toList())));
    addMissingKey(map);
    return map;
  }

  private void addMissingKey(Map<String, List<Trips>> map) {
    List<String> possibleValue = Arrays.asList("pending", "accepted", "refused");
    for (String s : possibleValue) {
      if (!map.containsKey(s)) {
        map.put(s, new ArrayList<>());
      }
    }
  }

  public Iterable<Trips> futureTripsByDriverId(long id) {
    return top20(repository.findAllByDepartureAfterAndIdDriver(LocalDate.now(), id));
  }

  public Iterable<Trips> futureTrips() {
    return top20(
        repository.findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfterOrderByIdTripDesc(1,
            LocalDate.now()));
  }

  public Iterable<Trips> tripsByDeparture(LocalDate date) {
    return top20(
        repository.findAllByAvaibleSeatingGreaterThanEqualAndDepartureOrderByIdTripDesc(1, date));
  }

  public Iterable<Trips> tripsByOriginAndDate(Double longitude, Double latitude,
      LocalDate date) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDeparture(1, date);
    return tripsOrderedByDestOrOrigin(trips, longitude, latitude, true);
  }

  public Iterable<Trips> tripsByDestinationAndDate(Double longitude, Double latitude,
      LocalDate date) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDeparture(1, date);
    return tripsOrderedByDestOrOrigin(trips, longitude, latitude, false);
  }

  public Iterable<Trips> tripsByOrigin(Double longitude, Double latitude) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfter(1,
        LocalDate.now());
    return tripsOrderedByDestOrOrigin(trips, longitude, latitude, true);
  }

  public Iterable<Trips> tripsByDestination(Double longitude, Double latitude) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfter(1,
        LocalDate.now());
    return tripsOrderedByDestOrOrigin(trips, longitude, latitude, false);
  }

  public Iterable<Trips> tripsByOriginAndDestination(Double longitudeOri,
      Double latitudeOri,
      Double longitudeDest, Double latitudeDest) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfter(1,
        LocalDate.now());
    var initialDist = gpsProxy.getGps(longitudeOri, latitudeOri, longitudeDest, latitudeDest);
    return tripsOrderedByOriginAndDist(trips, initialDist);
  }

  public Iterable<Trips> tripsByOriginAndDestinationAndDate(Double longitudeOri,
      Double latitudeOri,
      Double longitudeDest, Double latitudeDest, LocalDate date) {
    var trips = repository.findAllByAvaibleSeatingGreaterThanEqualAndDeparture(1, date);
    var initialDist = gpsProxy.getGps(longitudeOri, latitudeOri, longitudeDest, latitudeDest);
    return tripsOrderedByOriginAndDist(trips, initialDist);
  }

  private Iterable<Trips> top20(Iterable<Trips> trips) {
    return StreamSupport.stream(trips.spliterator(), false).limit(20).toList();
  }

  private Iterable<Trips> tripsOrderedByOriginAndDist(Iterable<Trips> trips, Double initialDist) {
    return StreamSupport.stream(trips.spliterator(), false).sorted(Comparator.comparingDouble
        (value -> gpsProxy.getGps(value.getOrigin().getLongitude(), value.getOrigin().getLatitude(),
            value.getDestination().getLongitude()
            , value.getDestination().getLatitude()) + initialDist)).limit(20).toList();
  }

  private Iterable<Trips> tripsOrderedByDestOrOrigin(Iterable<Trips> trips, Double longitude,
      Double latitude, boolean origin) {
    return origin ? StreamSupport.stream(trips.spliterator(), false)
        .sorted(Comparator.comparingDouble(
            value -> gpsProxy.getGps(longitude, latitude, value.getOrigin().getLongitude(),
                value.getOrigin().getLatitude()))).limit(20).toList()
        : StreamSupport.stream(trips.spliterator(), false).sorted(Comparator.comparingDouble(
            value -> gpsProxy.getGps(longitude, latitude, value.getDestination().getLongitude(),
                value.getDestination().getLatitude()))).limit(20).toList();
  }
}