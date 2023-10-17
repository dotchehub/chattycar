package be.vinci.trips.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "gps")
public interface GpsProxy {

  @GetMapping("/gps")
  Double getGps(@RequestParam(name = "originLong", required = false) Double originLon,
      @RequestParam(name = "originLat", required = false) Double originLat,
      @RequestParam(name = "destinationLong", required = false) Double destinationLon,
      @RequestParam(name = "destinationLat", required = false) Double destinationLat);
}