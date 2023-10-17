package be.vinci.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewTrip {
  private Position origin;
  private Position destination;
  private String departureDate;
  private Integer driverId;
  private Integer availableSeating;
}
