package br.edu.ifsul.cstsi.tcc_server.api.users;

import br.edu.ifsul.cstsi.tcc_server.api.histories.History;
import br.edu.ifsul.cstsi.tcc_server.api.ratings.Rating;
import br.edu.ifsul.cstsi.tcc_server.api.series.Serie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"favorites", "insertedSeries", "ratings", "histories", "roles"}) // FUTURAMENTE FAVORITES E RATINGS SAI DAQUI
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean isConfirmed = false;

    @ManyToMany(fetch = FetchType.EAGER) // BUSCAR ISSO
    @JoinTable(name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "serie_id"))
    private List<Serie> favorites; //Nenhum ou mais usuários favoritam nenhuma ou várias séries

    @OneToMany(mappedBy = "user") // NÃO BUSCAR ISSO (ROLE_ADMIN)
    private List<Serie> insertedSeries; //1 usuário cadastra várias séries

    @OneToMany(mappedBy = "user") // BUSCAR ISSO (ROLE_USER)
    List<Rating> ratings;

    @ManyToMany(fetch = FetchType.EAGER) // BUSCAR ISSO (ROLE_ADMIN)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isConfirmed=" + isConfirmed +
                ", roles=" + roles +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123"));
    }
}
