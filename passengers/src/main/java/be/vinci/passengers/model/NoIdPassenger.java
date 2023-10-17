package be.vinci.passengers.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoIdPassenger {
  @JsonProperty("passenger_id")
  private long idPassenger;
  @JsonProperty("trip_id")
  private long idTrips;
  @JsonProperty("status")
  private String status;

  public Passenger toPassenger(){
    return new Passenger(0L,idPassenger,idTrips,status);
  }
}
