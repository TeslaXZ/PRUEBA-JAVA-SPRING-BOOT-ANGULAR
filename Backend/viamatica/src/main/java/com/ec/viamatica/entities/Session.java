package com.ec.viamatica.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Setter
@Getter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEgreso;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
