package be.vinci.gateway.data;

import be.vinci.gateway.models.Passenger;

import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "passengers")
public interface PassengersProxy {

  @DeleteMapping("/passengers/trips/{trip_id}")
  void deleteAllPassengerByTripId(@PathVariable("trip_id") Integer tripId);


  @PostMapping("/passengers/{user_id}/trips/{trip_id}")
  void addPassengerToPendingTrip(
      @PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId
  );

  @GetMapping("/passengers/{user_id}/trips/{trip_id}")
  String getPassengerStatus(
      @PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId
  );

  @PutMapping("/passengers/{user_id}/trips/{trip_id}")
  void updatePassengerStatus(
      @PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId
  );

  @DeleteMapping("/passengers/{user_id}/trips/{trip_id}")
  void removePassengerFromTrip(
      @PathVariable("trip_id") Integer tripId,
      @PathVariable("user_id") Integer userId
  );

}
