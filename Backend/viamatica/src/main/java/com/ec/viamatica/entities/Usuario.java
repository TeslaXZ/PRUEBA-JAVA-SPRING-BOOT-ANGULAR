package com.ec.viamatica.entities;

import com.ec.viamatica.utils.Status;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
    @Enumerated(EnumType.STRING)
    private Status status;
}
