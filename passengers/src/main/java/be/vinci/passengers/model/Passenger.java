package be.vinci.passengers.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
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
@Entity(name = "passengers")
public class Passenger {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  @Column(name = "id")
  private long id;
  @JsonProperty("passenger_id")
  @Column(name = "passenger_id")
  private long idPassenger;
  @JsonProperty("trip_id")
  @Column(name = "trip_id")
    private long idTrips;
  @JsonProperty("status")
  @Column(name = "status")
  private String status;
}
