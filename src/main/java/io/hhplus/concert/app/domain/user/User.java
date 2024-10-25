package io.hhplus.concert.app.domain.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id; //id
    String name; //이름
    String password; //비밀번호
}
