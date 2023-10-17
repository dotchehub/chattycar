package be.vinci.trips.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "trips")
public class Trips {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("trip_id")
  @Column(name = "trip_id")
  private long idTrip;
  @JsonProperty("driver_id")
  @Column(name = "driver_id")
  private long idDriver;
  @JsonProperty("departure")
  @Column(name = "departure")
  private LocalDate departure;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "origin_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "origin_longitude")),
  })
  private Position origin;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude")),
  })
  private Position destination;
  @JsonProperty("available_seating")
  @Column(name = "available_seating")
  private int avaibleSeating;
}