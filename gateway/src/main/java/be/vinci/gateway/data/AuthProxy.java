package be.vinci.gateway.data;

import be.vinci.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "authentication")
public interface AuthProxy {

    @PostMapping("/authentication/connect")
    String connect(@RequestBody Credentials credentials);

    @PostMapping("/authentication/verify")
    String verify(@RequestBody String token);

    @PostMapping("/authentication")
    void createCredentials(@RequestBody Credentials credentials);

    @PutMapping("/authentication/{email}")
    void updateCredentials(@PathVariable String email, @RequestBody Credentials credentials);

    @DeleteMapping("/authentication/{email}")
    void deleteCredentials(@PathVariable String email);

}
