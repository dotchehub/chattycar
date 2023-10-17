package be.vinci.notification;

import be.vinci.notification.models.Notification;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification,Long> {

  Iterable<Notification> findByUserId(int idUser);

  @Transactional
  void deleteByUserId(int idUser);
}
