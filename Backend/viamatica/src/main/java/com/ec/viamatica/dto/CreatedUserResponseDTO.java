package com.ec.viamatica.dto;

import com.ec.viamatica.entities.Rol;
import com.ec.viamatica.entities.Usuario;
import com.ec.viamatica.utils.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CreatedUserResponseDTO(
        Long id,
        String nombres,
        String apellidos,
        String identificacion,
        LocalDateTime fechaDeNacimiento,
        String username,
        String mail,
        List<String> roles,
        Status status


        ) {
    public CreatedUserResponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getPersona().getNombres(),
                usuario.getPersona().getApellidos()
                ,usuario.getPersona().getIdentificacion(),
                usuario.getPersona().getFechaDeNacimiento(),
                usuario.getUsername(),
                usuario.getMail(), usuario.getRoles().stream().map(Rol::getName).toList(),
                usuario.getStatus()
                );
    }



}
