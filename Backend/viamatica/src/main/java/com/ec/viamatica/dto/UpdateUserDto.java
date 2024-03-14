package com.ec.viamatica.dto;

public record UpdateUserDto(
        String password,
        String nombres,
        String apellidos,
        String identificacion
) {
}
