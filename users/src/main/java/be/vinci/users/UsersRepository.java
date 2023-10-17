package be.vinci.users;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {

  /**
   * Checks if the user exists
   * @param email user's email
   * @return True if there is no user with the email, false if there is an user with the email
   */
  boolean existsByEmail(String email);
  User findByEmail(String email);
}
