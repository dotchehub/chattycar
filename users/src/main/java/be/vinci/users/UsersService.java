package be.vinci.users;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private final UsersRepository repository;

  public UsersService(UsersRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates an user
   * @param user user to create
   * @return true if the user could be created, false if another user exists with this email
   */
  public User createOne(User user) {
    if (repository.existsByEmail(user.getEmail())) return null;
    return repository.save(user);
  }

  /**
   * Reads an user
   * @param email user's email
   * @return The user found or null if the user has not been found
   */
  public User readOne(String email) {
    return repository.findByEmail(email);
  }

  /**
   * Reads an user
   * @param id user's id
   * @return The user found or null if the user has not been found
   */
  public User readOne(int id) {
    return repository.findById(id).orElse(null);
  }

  /**
   * Updates an user
   * @param user user to update
   * @return True if the user could be updated, false if the user couldn't be found
   */
  public boolean updateOne(User user) {
    User found = repository.findById(user.getId()).orElse(null);
    if (found == null) return false;
    repository.save(user);
    return true;
  }

  /**
   * Deletes an user
   * @param id user's id
   * @return True if the user could be deleted, false if the user couldn't be found
   */
  public boolean deleteOne(int id) {
    if (!repository.existsById(id)) return false;
    repository.deleteById(id);
    return true;
  }

}
