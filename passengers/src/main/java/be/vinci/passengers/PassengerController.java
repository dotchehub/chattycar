package be.vinci.passengers;

import be.vinci.passengers.model.Passenger;
import be.vinci.passengers.model.User;
import java.util.List;
import java.util.Map;
import javax.ws.rs.QueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PassengerController {

  private final PassengerService service;

  public PassengerController(PassengerService service) {
    this.service = service;
  }

  @GetMapping("/passengers/users/{id}")
  public ResponseEntity<Iterable<Passenger>> findTripsWhereImPassenger(
      @PathVariable("id") long passengerId) {
    var passengers = service.findTripsWhereImPassenger(passengerId);
    return new ResponseEntity<>(passengers, HttpStatus.OK);
  }

  @GetMapping("/passengers/trips/{id}")
  public ResponseEntity<Map<String, List<User>>> findAll(@PathVariable("id")
      long tripId) {
    var passengers = service.findAll(tripId);
    return new ResponseEntity<>(passengers, HttpStatus.OK);
  }

  @PostMapping("/passengers/{user_id}/trips/{trip_id}")
  public HttpStatus addPassangerToTrip(@PathVariable("user_id") long userId,
      @PathVariable("trip_id") long tripId) {
    if (service.existByIdPassengerAndIdTrips(userId, tripId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    service.addPassangerToTrip(userId, tripId);
    return HttpStatus.OK;
  }

  @GetMapping("/passengers/{user_id}/trips/{trip_id}")
  public ResponseEntity<String> getPassengerStatus(@PathVariable("user_id") long userId,
      @PathVariable("trip_id") long tripId) {
    var passengerStatus = service.getPassengerStatus(userId, tripId);
    if (passengerStatus == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(service.getPassengerStatus(userId, tripId), HttpStatus.OK);
  }

  @PutMapping("/passengers/{user_id}/trips/{trip_id}")
  public HttpStatus updatePassengerStatus(@PathVariable("user_id") long userId,
      @PathVariable("trip_id") long tripId, @QueryParam("status") String status) {
    if ((!status.equals("accepted") && !status.equals("refused")) || !service.existByIdPassenger(
        userId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!service.existByIdPassengerAndIdTrips(userId, tripId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    service.updatePassengerStatus(userId, tripId, status);
    return HttpStatus.OK;
  }

  @DeleteMapping("/passengers/trips/{trip_id}")
  public HttpStatus deleteAllPassengerByTripId(@PathVariable("trip_id") long tripId) {
    service.deleteAllPassengerByTripId(tripId);
    return HttpStatus.OK;
  }

  @DeleteMapping("/passengers/{user_id}/trips")
  public HttpStatus deleteAllTripsOfaUser(@PathVariable("user_id") long userId) {
    if (!service.existByIdPassenger(userId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    service.deleteAllTripsOfaUser(userId);
    return HttpStatus.OK;
  }

  @DeleteMapping("/passengers/{user_id}/trips/{trip_id}")
  public HttpStatus deletePassengerFromATrip(@PathVariable("user_id") long userId,
      @PathVariable("trip_id") long tripId) {
    if (!service.existByIdPassenger(userId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (!service.existByIdPassengerAndIdTrips(userId, tripId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    service.deletePassengerFromTrip(userId, tripId);
    return HttpStatus.OK;
  }
}