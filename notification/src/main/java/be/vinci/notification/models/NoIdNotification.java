package be.vinci.notification.models;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoIdNotification {
  private Integer userId;
  private Integer tripId;
  private LocalDate date;
  private String notificationText;

  public Notification toNotification(){
    return new Notification(0, userId, tripId, date,notificationText);
  }
}
