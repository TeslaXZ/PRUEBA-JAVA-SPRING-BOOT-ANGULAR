package com.ec.viamatica.entities;

import com.ec.viamatica.utils.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
@Getter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String mail;
    private boolean sessionActive;
    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
    @ManyToMany
    @JoinTable(
            name = "usuario_rol", //
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "usuario")
    private List<Session> session;
    private Integer loginAttempts = 0;
}
