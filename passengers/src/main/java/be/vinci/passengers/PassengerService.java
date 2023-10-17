package be.vinci.passengers;

import be.vinci.passengers.data.PassengerRepository;
import be.vinci.passengers.data.UsersProxy;
import be.vinci.passengers.model.NoIdPassenger;
import be.vinci.passengers.model.Passenger;
import be.vinci.passengers.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

  private final PassengerRepository repository;
  private final UsersProxy usersProxy;

  public PassengerService(PassengerRepository repository,
      UsersProxy usersProxy) {
    this.repository = repository;
    this.usersProxy = usersProxy;
  }

  public Map<String, List<User>> findAll(long tripId) {
    var allPassengers = repository.findAllByIdTrips(tripId);
    var map = StreamSupport.stream(allPassengers.spliterator(), false)
        .collect(Collectors.groupingBy(Passenger::getStatus,
            Collectors.mapping(d -> usersProxy.getOneById((int) d.getIdPassenger()),
                Collectors.toList())));
    addMissingKey(map);
    return map;
  }

  private void addMissingKey(Map<String, List<User>> map) {
    List<String> possibleValue = Arrays.asList("pending", "accepted", "refused");
    for (String s : possibleValue) {
      if (!map.containsKey(s)) {
        map.put(s, new ArrayList<>());
      }
    }
  }

  public Iterable<Passenger> findTripsWhereImPassenger(long idPassenger) {
    return repository.findAllByIdPassenger(idPassenger);
  }

  public void addPassangerToTrip(long userId, long tripId) {
    var pass = new NoIdPassenger();
    pass.setIdPassenger(userId);
    pass.setStatus("pending");
    pass.setIdTrips(tripId);
    repository.save(pass.toPassenger());
  }

  public String getPassengerStatus(long userId, long tripId) {
    var passenger = repository.findByIdTripsAndIdPassenger(tripId, userId);
    if (passenger == null) {
      return null;
    }
    return passenger.getStatus();
  }

  public boolean existByIdPassengerAndIdTrips(long idPass, long tripsId) {
    return repository.existsByIdPassengerAndIdTrips(idPass, tripsId);
  }

  public boolean existByIdPassenger(long idPass) {
    return repository.existsByIdPassenger(idPass);
  }

  public void updatePassengerStatus(long userId, long tripId, String status) {
    var passengerUpdated = repository.findByIdTripsAndIdPassenger(tripId, userId);
    passengerUpdated.setStatus(status);
    repository.save(passengerUpdated);
  }

  public void deletePassengerFromTrip(long userId, long tripId) {
    repository.delete(repository.findByIdTripsAndIdPassenger(tripId, userId));
  }

  public void deleteAllTripsOfaUser(long userId) {
    repository.deleteAllByIdPassenger(userId);
  }

  public void deleteAllPassengerByTripId(long tripId) {
    repository.deleteAllByIdTrips(tripId);
  }
}