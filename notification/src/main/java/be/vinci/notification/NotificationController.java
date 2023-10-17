package be.vinci.notification;

import be.vinci.notification.models.NoIdNotification;
import be.vinci.notification.models.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class NotificationController {
  private final NotificationService service;

  public NotificationController(NotificationService service){this.service=service;}

  @PostMapping("/notifications")
  public ResponseEntity<Notification> createOne(@RequestBody NoIdNotification notification){
    if (notification.getNotificationText()== null || notification.getTripId()<=0 || notification.getUserId()<=0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    Notification created = service.createOne(notification);
    if (created== null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(created,HttpStatus.CREATED);
  }

  @GetMapping("/notifications/user/{id}")
  public ResponseEntity<Iterable<Notification>> readFrom(@PathVariable int id) {
    Iterable<Notification> notifications = service.readFromUser(id);
    if (notifications == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(notifications,HttpStatus.OK);

  }


  @DeleteMapping("/notifications/user/{id}")
  public ResponseEntity<Void> deleteFromUser(@PathVariable int id){
    service.deleteFromUser(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
