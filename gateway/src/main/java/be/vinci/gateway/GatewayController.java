package be.vinci.gateway;

import be.vinci.gateway.models.Credentials;
import be.vinci.gateway.models.NewTrip;
import be.vinci.gateway.models.NewUser;
import be.vinci.gateway.models.Notification;
import be.vinci.gateway.models.Passenger;
import be.vinci.gateway.models.Trip;
import be.vinci.gateway.models.User;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = { "https://www.chattycar.com", "http://localhost" })
@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }

    @PostMapping("/authentication")
    String connect(@RequestBody Credentials credentials) { return service.connect(credentials); }

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestBody NewUser user) {
        System.out.println("OKKKKKKK");
        service.createUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    User readUser(@RequestParam("email") String email) {
        return service.readUser(email);
    }

    @PutMapping("/users")
    ResponseEntity<Void> updateCredentials(@RequestBody Credentials credentials,@RequestHeader("Authorization") String token) {
        if(credentials.getEmail()==null||credentials.getPassword()==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if(token==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        User user = service.readUser(credentials.getEmail());
        if(user==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        String userEmail = service.verify(token);

        if(!(credentials.getEmail().equals(userEmail))) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        service.updateCredential(credentials);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    User readUser(@PathVariable int id){
        return service.readUser(id);
    }

    @PutMapping("/users/{id}")
    void updateUser(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token) {
        if (!user.getId().equals(id)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        String userEmail = service.verify(token);
        User userFound = service.readUser(userEmail);
        if (userFound.getId()!=id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        service.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id, @RequestHeader("Authorization") String token) {
        String userEmail = service.verify(token);
        User userFround = service.readUser(userEmail);
        if (userFround.getId()!=id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        service.deleteUser(userFround);
    }

    @GetMapping("/user/{id}/notifications")
    public ResponseEntity<Iterable<Notification>> readUserNotifications(@PathVariable int id){
        Iterable<Notification> notifications = service.readUserNotifications(id);
        return new ResponseEntity<>(notifications,HttpStatus.OK);
    }
    @DeleteMapping("/user/{id}/notifications")
    public ResponseEntity<Void> deleteUserNotification(@PathVariable int id){
        service.deleteUserNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/trips")
    ResponseEntity<Trip> createTrip(
        @RequestHeader("Authorization") String token,
        @RequestBody NewTrip newTrip
    ) {
        String userInfo = service.verify(token);
        User driver = service.readUser(newTrip.getDriverId());
        if(driver.getEmail().equals(userInfo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return service.createTrip(newTrip);
    }

    @GetMapping("/trips")
    ResponseEntity<Iterable<Trip>> getAllTrips(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(name = "departure",required = false)
            LocalDate departure,
        @RequestParam Double originLat,
        @RequestParam Double originLon,
        @RequestParam Double destinationLat,
        @RequestParam Double destinationLon
    ) {
        return service.getAllTrips(
            departure,
            originLat,
            originLon,
            destinationLat,
            destinationLon
        );
    }

    @GetMapping("/trips/{id}")
    ResponseEntity<Trip> getTrip(
        @PathVariable Integer id
    ) {
        return service.getTripById(id);
    }

    @DeleteMapping("/trips/{id}")
    void deleteTripAndListOfPassengers(
        @RequestHeader("Authorization") String token,
        @PathVariable Integer id
    ) {
        checkDriverToken(token, id);

        service.deleteTripAndListOfPassengers(id);
    }

    @GetMapping("/trips/{id}/passengers")
    ResponseEntity<Iterable<Passenger>> getPassengersFromTrip(
        @RequestHeader("Authorization") String token,
        @PathVariable Integer id
    ) {
        checkDriverToken(token, id);

        return service.getPassengersFromTrip(id);
    }

    @PostMapping("/trips/{trip_id}/passengers/{user_id}")
    void addPassengerToPendingTrip(
        @RequestHeader("Authorization") String token,
        @PathVariable("trip_id") Integer tripId,
        @PathVariable("user_id") Integer userId
    ) {
        checkDriverToken(token, tripId);

        service.addPassengerToPendingTrip(userId, tripId);
    }

    @GetMapping("/trips/{trip_id}/passengers/{user_id}")
    String getPassengerStatus(
        @RequestHeader("Authorization") String token,
        @PathVariable("trip_id") Integer tripId,
        @PathVariable("user_id") Integer userId
    ) {
        checkDriverToken(token, tripId);

        return service.getPassengerStatus(userId, tripId);
    }

    @PutMapping("/trips/{trip_id}/passengers/{user_id}")
    void updatePassengerStatus(
        @RequestHeader("Authorization") String token,
        @PathVariable("trip_id") Integer tripId,
        @PathVariable("user_id") Integer userId
    ) {
        checkDriverToken(token, tripId);

        service.updatePassengerStatus(tripId, userId);
    }

    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}")
    void removePassengerFromTrip(
        @RequestHeader("Authorization") String token,
        @PathVariable("trip_id") Integer tripId,
        @PathVariable("user_id") Integer userId
    ) {
        checkDriverToken(token, tripId);

        service.removePassengerFromTrip(tripId, userId);
    }

    private void checkDriverToken(
        @RequestHeader("Authorization") String token,
        @PathVariable("trip_id") Integer tripId) {
        String userInfo = service.verify(token);
        Trip trip = service.getTripById(tripId).getBody();
        if(trip == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        User driver = service.readUser(trip.getDriverId());
        if(driver.getEmail().equals(userInfo)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

}
