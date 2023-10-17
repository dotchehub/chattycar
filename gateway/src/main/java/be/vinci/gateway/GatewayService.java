package be.vinci.gateway;

import be.vinci.gateway.data.AuthProxy;
import be.vinci.gateway.data.GpsProxy;
import be.vinci.gateway.data.UsersProxy;
import be.vinci.gateway.data.NotificationProxy;
import be.vinci.gateway.data.PassengersProxy;
import be.vinci.gateway.data.TripsProxy;
import be.vinci.gateway.models.Credentials;
import be.vinci.gateway.models.NewTrip;
import be.vinci.gateway.models.NewUser;
import be.vinci.gateway.models.Notification;
import be.vinci.gateway.models.Passenger;
import be.vinci.gateway.models.Trip;
import be.vinci.gateway.models.User;

import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {

    private final AuthProxy authProxy;
    private final UsersProxy usersProxy;
    private final NotificationProxy notificationProxy;
    private final PassengersProxy passengersProxy;
    private final TripsProxy tripsProxy;


    public GatewayService(AuthProxy authProxy,
                          UsersProxy usersProxy,
                          NotificationProxy notificationProxy,
                          PassengersProxy passengersProxy,
                          TripsProxy tripsProxy
    ) {
        this.authProxy = authProxy;
        this.usersProxy = usersProxy;
        this.notificationProxy = notificationProxy;
        this.passengersProxy = passengersProxy;
        this.tripsProxy = tripsProxy;
    }
    public void createUser(NewUser user) {
        //usersProxy.createUser(user.toUser());
        authProxy.createCredentials(user.toCredentials());
    }
    public String connect(Credentials credentials) {
        return authProxy.connect(credentials);
    }

    public User readUser(String email){
        return usersProxy.readUser(email);
    }

    public User readUser(int id){
        return usersProxy.readUser(id);
    }

    public void updateUser(User user){
        usersProxy.updateUser(user.getId(),user);
    }

    public void deleteUser(User user){
        authProxy.deleteCredentials(user.getEmail());
        notificationProxy.deleteNotifications(user.getId());
        usersProxy.deleteUser(user.getId());
    }
    public void updateCredential(Credentials credentials){
        authProxy.updateCredentials(credentials.getEmail(),credentials);
    }

    public Iterable<Notification> readUserNotifications(int id){
        return notificationProxy.getNotifications(id);
    }

    public void deleteUserNotification(int id){
        notificationProxy.deleteNotifications(id);
    }

    public String verify(String token) {
        return authProxy.verify(token);
    }


    public ResponseEntity<Trip> createTrip(NewTrip newTrip) {
        return tripsProxy.createTrip(newTrip);
    }

    public ResponseEntity<Iterable<Trip>> getAllTrips(
        LocalDate departure,
        Double originLat,
        Double originLon,
        Double destinationLat,
        Double destinationLon) {
        return tripsProxy.getAllTrips(departure, originLat, originLon, destinationLat, destinationLon);
    }

    public ResponseEntity<Trip> getTripById(Integer id) {
        return tripsProxy.getTripById(id);
    }

    public void deleteTripAndListOfPassengers(Integer id) {
        passengersProxy.deleteAllPassengerByTripId(id);
        tripsProxy.deleteTrip(id);
    }

    public ResponseEntity<Iterable<Passenger>> getPassengersFromTrip(Integer id) {
        return tripsProxy.getPassengersFromTrip(id);
    }

    public void addPassengerToPendingTrip(Integer userId, Integer tripId) {
        passengersProxy.addPassengerToPendingTrip(userId, tripId);
    }

    public String getPassengerStatus(Integer userId, Integer tripId) {
        return passengersProxy.getPassengerStatus(userId, tripId);
    }

    public void updatePassengerStatus(Integer tripId, Integer userId) {
        passengersProxy.updatePassengerStatus(userId, tripId);
    }

    public void removePassengerFromTrip(Integer tripId, Integer userId) {
        passengersProxy.removePassengerFromTrip(tripId, userId);
    }
}
