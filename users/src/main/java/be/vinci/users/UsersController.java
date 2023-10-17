package be.vinci.users;

import feign.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {

  private final UsersService service;

  public UsersController(UsersService service) {
    this.service = service;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createOne(@RequestBody User newUser) {
    if (newUser.getEmail() == null || newUser.getFirstname() == null
        || newUser.getLastname() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    User created = service.createOne(newUser);
    if (created == null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/users")
  public ResponseEntity<User> readOne(@RequestParam("email") String email) {
    User user = service.readOne(email);
    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> readOne(@PathVariable int id) {
    User found = service.readOne(id);
    if (found==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(found, HttpStatus.OK);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<Void> updateOne(@PathVariable int id, @RequestBody User user) {
    if (user==null || user.getEmail()==null || user.getLastname()==null ||
        user.getFirstname()==null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    boolean found = service.updateOne(user);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteOne(@PathVariable int id) {
    boolean deleted = service.deleteOne(id);
    if (!deleted) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
