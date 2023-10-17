package be.vinci.trips.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Passenger {
  private long id;
  @JsonProperty("passenger_id")
  private long idPassenger;
  @JsonProperty("trip_id")
  private long idTrips;
  private String status;
}
