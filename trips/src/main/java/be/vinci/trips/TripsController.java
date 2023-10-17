package be.vinci.trips;

import be.vinci.trips.models.NoIdTrips;
import be.vinci.trips.models.Trips;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TripsController {

  private final TripsService service;

  public TripsController(TripsService service) {
    this.service = service;
  }

  @PostMapping("/trips")
  public ResponseEntity<Trips> createOne(@RequestBody NoIdTrips trips) {
    if (trips.getDriverId() <= 0 || trips.getAvailableSeating() <=0 || trips.getDeparture().isBefore(LocalDate.now())
        || trips.getOrigin() == null || trips.getDestination() == null ){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(service.createOne(trips), HttpStatus.CREATED);
  }

  @GetMapping("/trips")
  public ResponseEntity<Iterable<Trips>> readAll(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "departure",required = false)
          LocalDate departure,
      @RequestParam(name = "originLon",required = false) Double originLon,
      @RequestParam(name = "originLat",required = false) Double originLat,
      @RequestParam(name = "destLon",required = false) Double destinationLon,
      @RequestParam(name = "destLat",required = false) Double destinationLat) {
    if ((originLon==null && originLat != null) || (originLon!=null && originLat == null)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if ((destinationLon==null && destinationLat != null) || (destinationLon!=null && destinationLat == null)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (departure != null) {
      // only by departure
      if (originLon == null && destinationLon == null) {
        return new ResponseEntity<>(service.tripsByDeparture(departure),HttpStatus.OK);
      }
      // date + dest + origin
      if (originLon != null && destinationLon != null) {
        return new ResponseEntity<>(service.tripsByOriginAndDestinationAndDate(originLon, originLat, destinationLon,
            destinationLat, departure),HttpStatus.OK);
      }
      // date + origin
      if (originLon != null) {
        return new ResponseEntity<>(service.tripsByOriginAndDate(originLon, originLat, departure),HttpStatus.OK);
      }
      //date + dest
      return new ResponseEntity<>(service.tripsByDestinationAndDate(destinationLon, destinationLat, departure),HttpStatus.OK);
    } else {
      // 20 future trips
      if (originLon == null && destinationLon == null) {
        return new ResponseEntity<>(service.futureTrips(),HttpStatus.OK);
      }
      // dest + origin
      if (originLon != null && destinationLon != null) {
        return new ResponseEntity<>(service.tripsByOriginAndDestination(originLon, originLat, destinationLon,
            destinationLat),HttpStatus.OK);
      }
      //  origin
      if (originLon != null) {
        return new ResponseEntity<>(service.tripsByOrigin(originLon, originLat),HttpStatus.OK);
      }
      // dest
      return new ResponseEntity<>(service.tripsByDestination(destinationLon, destinationLat),HttpStatus.OK);
    }
  }

  @GetMapping("/trips/{id}")
  public ResponseEntity<Trips> getOne(@PathVariable("id") long id) {
    var trip = service.getOne(id);
    if (trip == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(trip, HttpStatus.OK);
  }

  @DeleteMapping("/trips/{id}")
  public HttpStatus deleteOne(@PathVariable("id") long id) {
    if (!service.deleteOne(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return HttpStatus.OK;
  }

  @GetMapping("/trips/users/{id}/driver")
  public ResponseEntity<Iterable<Trips>> getFutureTripsByDriverId(@PathVariable("id") long id) {
    return new ResponseEntity<>(service.futureTripsByDriverId(id), HttpStatus.OK);
  }

  @DeleteMapping("/trips/users/{id}/driver")
  public HttpStatus deleteAllDriverTrips(@PathVariable("id") long driverId) {
    service.deleteAllDriverTrips(driverId);
    return HttpStatus.OK;
  }

  @GetMapping("/trips/users/{id}/passenger")
  public ResponseEntity<Map<String, List<Trips>>> passengerTripsByStatus(
      @PathVariable("id") long id) {
    var trips = service.passengerTripsByStatus(id);
    if (trips == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(trips, HttpStatus.OK);
  }
}