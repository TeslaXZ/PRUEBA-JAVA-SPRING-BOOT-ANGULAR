package com.ec.viamatica.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rol_Opciones")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
@Getter
public class RolOpciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "rol_Opciones_rol", //
            joinColumns = @JoinColumn(name = "rolOpciones_idOpcion"),
            inverseJoinColumns = @JoinColumn(name = "rol_idRol")
    )
    private List<Rol> rol;
}
