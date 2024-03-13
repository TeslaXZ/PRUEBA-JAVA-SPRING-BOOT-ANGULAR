package com.ec.viamatica.controller;

import com.ec.viamatica.dto.CreateUsuarioDTO;
import com.ec.viamatica.dto.CreatedUserResponseDTO;
import com.ec.viamatica.dto.LoginUserDTO;
import com.ec.viamatica.dto.UpdateUserDto;
import com.ec.viamatica.service.UsuarioService;
import com.ec.viamatica.utils.Status;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final UsuarioService usuarioService;

    @PostMapping("saveUser")
    public ResponseEntity<String> createUser(@RequestBody CreateUsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.createUsuario(usuarioDTO));
    }

    @PostMapping ResponseEntity<String> Login(@RequestBody LoginUserDTO loginUserDTO, HttpServletResponse httpServletResponse){
            usuarioService.LoginUser(loginUserDTO, httpServletResponse);
            return ResponseEntity.ok("Inicio de sesion Existoso");
    }
    @GetMapping("/All")
    public ResponseEntity <Page<CreatedUserResponseDTO>> getAllUsers(@RequestParam(name = "status",required = false) Status status, Pageable pageable){
        return  ResponseEntity.ok(usuarioService.getAllUsers(pageable, status));
    }
    @GetMapping("/{id}")
    public  ResponseEntity<CreatedUserResponseDTO> getUserById (@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.getUserById(id));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserDto updateUserDto, @RequestParam Long id){
        usuarioService.updateUser(updateUserDto,id);
        return ResponseEntity.ok("Cambios guardados exitosamente");
    }
    @PutMapping("/deactivateUser")
    @Transactional
    public ResponseEntity<String> deactivateUser(@RequestParam Long id){
        usuarioService.deactivateUser(id);
        return ResponseEntity.ok("Usuario desactivado con exito");
    }


}
