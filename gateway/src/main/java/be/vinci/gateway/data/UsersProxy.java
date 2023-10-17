package be.vinci.gateway.data;

import be.vinci.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {

    @PostMapping("/users")
    void createUser(@RequestBody User user);

    @GetMapping("/users")
    User readUser(@RequestParam("email") String email);

    @GetMapping("/users/{id}")
    User readUser(@PathVariable int id);


    @PutMapping("/users/{id}")
    void updateUser(@PathVariable int id, @RequestBody User user);

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id);

}
