package be.vinci.gateway.data;

import be.vinci.gateway.models.Notification;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "notifications")
public interface NotificationProxy {

    @PostMapping("/notifications")
    String createOne(@RequestBody Notification notification);

    @GetMapping("/notifications/users/{id}")
    Iterable<Notification> getNotifications(@PathVariable int id);

    @DeleteMapping("/notifications/users/{id}")
    String deleteNotifications(@PathVariable int id);

}
