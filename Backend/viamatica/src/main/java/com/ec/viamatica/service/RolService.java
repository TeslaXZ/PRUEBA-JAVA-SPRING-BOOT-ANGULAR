package com.ec.viamatica.service;

import com.ec.viamatica.entities.Rol;
import com.ec.viamatica.exceptions.RolNotFoundException;
import com.ec.viamatica.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;

    public Rol findRolByName(Rol role){
        Optional<Rol> optionalRol = rolRepository.findByName(role.getName());
        if (optionalRol.isEmpty()){
            throw new RolNotFoundException("Rol no aceptado, un usuario solo puede tener rol, User o admin");
        }
        return optionalRol.get();
    }
}
