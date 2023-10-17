package be.vinci.trips.data;

import be.vinci.trips.models.Passenger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "passengers")
public interface PassengerProxy {
  @GetMapping("/passengers/users/{id}")
  ResponseEntity<Iterable<Passenger>> findTripWhereImPassenger(@PathVariable("id")
      long passengerId);
}
