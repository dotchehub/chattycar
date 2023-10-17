package be.vinci.gateway.data;

import be.vinci.gateway.models.Position;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "gps")
public interface GpsProxy {
    /*
    @GetMapping("/gps")
    Position getPos(
            @RequestBody Float originLat,
            @RequestBody Float originLon,
            @RequestBody Float destinationLat,
            @RequestBody Float destinationLon
    );
*/
}
