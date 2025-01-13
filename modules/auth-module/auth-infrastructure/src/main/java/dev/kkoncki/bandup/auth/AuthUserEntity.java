package dev.kkoncki.bandup.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auth_user", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserEntity {

    @Id
    private String id;

    private String email;

    private String password;
}
