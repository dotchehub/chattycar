package be.vinci.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
  private Integer id;
  private Position origin;
  private Position destination;
  private String departure;
  private Integer driverId;
  private Integer availableSeating;

}
