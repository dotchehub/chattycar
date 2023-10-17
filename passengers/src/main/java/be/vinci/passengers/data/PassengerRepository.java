package be.vinci.passengers.data;

import be.vinci.passengers.model.Passenger;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger,Long> {
  Iterable<Passenger> findAllByIdTrips(long idTrips);
  Passenger findByIdTripsAndIdPassenger(long idTrips,long IdPassenger);
  Iterable<Passenger> findAllByIdPassenger(long passengerId);
  boolean existsByIdPassengerAndIdTrips(long passengerId,long tripsId );
  boolean existsByIdPassenger(long idPassenger);
  @Transactional
  void deleteAllByIdPassenger(long passengerId);
  @Transactional
  void deleteAllByIdTrips(long tripsId);
}