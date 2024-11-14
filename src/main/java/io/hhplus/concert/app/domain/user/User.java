package io.hhplus.concert.app.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id; //id
    String name; //이름
}
