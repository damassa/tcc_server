package br.edu.ifsul.cstsi.tcc_server.api.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token_confirm_email")
@Getter
@Setter
@NoArgsConstructor
public class TokenConfirmEmail { //TODO: Rever sexta
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public TokenConfirmEmail(User user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }
}
