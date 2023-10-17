package be.vinci.trips.data;

import be.vinci.trips.models.Trips;
import java.time.LocalDate;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripsRepository extends CrudRepository<Trips, Long> {
  Iterable<Trips> findAllByDepartureAfterAndIdDriver(LocalDate date,long idDriver);
  Iterable<Trips> findAllByAvaibleSeatingGreaterThanEqualAndDepartureOrderByIdTripDesc(int avaibleSeat,LocalDate date);
  Iterable<Trips> findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfterOrderByIdTripDesc(int avaibleSeat,LocalDate date);
  Iterable<Trips> findAllByAvaibleSeatingGreaterThanEqualAndDeparture(int avaibleSeat,LocalDate date);
  Iterable<Trips> findAllByAvaibleSeatingGreaterThanEqualAndDepartureAfter(int avaibleSeat,LocalDate date);
  @Transactional
  void deleteAllByIdDriver(long driverId);
}
