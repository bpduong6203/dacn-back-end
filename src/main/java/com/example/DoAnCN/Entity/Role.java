package com.example.DoAnCN.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<User> users = new HashSet<>();
    @Override
    public String getAuthority() {
        return name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return
                false;
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
