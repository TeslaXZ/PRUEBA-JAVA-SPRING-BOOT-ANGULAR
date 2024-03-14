package com.ec.viamatica.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(

        String username,
        String mail,
        @NotBlank
        String password
) {
}
