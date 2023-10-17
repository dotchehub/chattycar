package be.vinci.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
  private Iterable<User> pending;
  private Iterable<User> accepter;
  private Iterable<User> refused;
}
