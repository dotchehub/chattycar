package be.vinci.gateway.models;

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
public class PassengerTrips {
  private Iterable<Trip> pending;
  private Iterable<Trip> accepter;
  private Iterable<Trip> refused;
}
