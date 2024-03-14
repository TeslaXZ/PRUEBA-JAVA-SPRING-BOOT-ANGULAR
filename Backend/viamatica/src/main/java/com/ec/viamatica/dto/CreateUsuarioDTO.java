package com.ec.viamatica.dto;

import com.ec.viamatica.entities.Persona;
import com.ec.viamatica.entities.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateUsuarioDTO(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String nombres,
        @NotBlank
        String apellidos,
        @NotBlank
        String identificacion,
        @NotNull
        LocalDateTime fechaDeNacimiento,
        @NotNull
        List<Rol> roles
) {
}
