package be.vinci.trips.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoIdTrips {
  @JsonProperty("driver_id")
  private long driverId;
  private LocalDate departure;
  private Position origin;
  private Position destination;
  @JsonProperty("available_seating")
  private int availableSeating;

  public Trips toTrips() {
    return new Trips(0L,driverId,departure,origin,destination,availableSeating);
  }
}
