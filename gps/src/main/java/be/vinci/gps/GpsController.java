package be.vinci.gps;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GpsController {

    @GetMapping("/gps")
    public ResponseEntity<Double> distance(@RequestParam("originLat") Double originLat,
                                           @RequestParam("originLong") Double originLong,
                                           @RequestParam("destinationLat") Double destinationLat,
                                           @RequestParam("destinationLong") Double destinationLong) {
        if (originLat==null || originLong==null || destinationLat==null || destinationLong==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        originLong = Math.toRadians(originLong);
        originLat = Math.toRadians(originLat);
        destinationLong = Math.toRadians(destinationLong);
        destinationLat = Math.toRadians(destinationLat);

        double longitude = destinationLong - originLong;
        double latitude = destinationLat - originLat;
        double a = 2 * Math.pow(Math.sin(latitude / 2), 2)
                + Math.cos(originLat) * Math.cos(destinationLat)
                * Math.pow(Math.sin(longitude / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 3956; // in km
        double distance = c * r;
        return new ResponseEntity<>(distance, HttpStatus.OK);
    }
}
