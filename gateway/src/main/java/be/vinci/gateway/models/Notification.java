package be.vinci.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
  private Integer userId;
  private Integer tripId;
  private String date;
  private String notificationText;
}
