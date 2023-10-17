package be.vinci.gateway.data;

import be.vinci.gateway.models.NewTrip;
import be.vinci.gateway.models.Passenger;
import be.vinci.gateway.models.Trip;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "trips")
public interface TripsProxy {

    @PostMapping("/trips")
    ResponseEntity<Trip> createTrip(@RequestBody NewTrip newTrip);

    @GetMapping("/trips")
    ResponseEntity<Iterable<Trip>> getAllTrips(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "departure",required = false)
            LocalDate departure,
        @RequestParam Double originLat,
        @RequestParam Double originLon,
        @RequestParam Double destinationLat,
        @RequestParam Double destinationLon
    );

    @GetMapping("/trips/{id}")
    ResponseEntity<Trip> getTripById(Integer id);

    @DeleteMapping("/trips/{id}")
    void deleteTrip(@PathVariable Integer id);

    @GetMapping("/trips/{id}/passengers")
    ResponseEntity<Iterable<Passenger>> getPassengersFromTrip(Integer id);

}
