package be.vinci.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Integer id;
  private String email;
  private String firstname;
  private String lastname;
}
