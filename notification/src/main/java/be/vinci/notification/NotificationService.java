package be.vinci.notification;

import be.vinci.notification.models.NoIdNotification;
import be.vinci.notification.models.Notification;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {
  private final NotificationRepository repository;


  public NotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a user
   * @param notification User to create
   * @return true if the user could be created, false if another user exists with this pseudo
   */
  public Notification createOne(NoIdNotification notification) {
    return repository.save(notification.toNotification());
  }

  /**
   * Reads all user notifications in repository
   * @param id ID of the user
   * @return all notifications
   */
  public Iterable<Notification> readFromUser(int id) {
    //e.forEach((element)-> {repository.deleteByIdUser(idUser);});
   return repository.findByUserId(id);
  }

  /**
   * Deletes all notifications of the user
   * @param id  of the user
   */
  public void deleteFromUser(int id){
    repository.deleteByUserId(id);
  }






}
