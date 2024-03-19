package com.ec.viamatica.service;

import com.ec.viamatica.dto.*;
import com.ec.viamatica.entities.Persona;
import com.ec.viamatica.entities.Session;
import com.ec.viamatica.entities.Usuario;
import com.ec.viamatica.exceptions.*;
import com.ec.viamatica.repositories.PersonaRepository;
import com.ec.viamatica.repositories.UsuarioRepository;
import com.ec.viamatica.utils.Status;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolService rolService;
    private final SessionService sessionService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public String createUsuario(CreateUsuarioDTO usuarioDTO){
        Persona persona = new Persona();
        persona.setNombres(usuarioDTO.nombres());
        persona.setApellidos(usuarioDTO.apellidos());
        persona.setIdentificacion(validateIdentificationNumber(usuarioDTO.identificacion()));
        persona.setFechaDeNacimiento(usuarioDTO.fechaDeNacimiento());
        personaRepository.save(persona);
        Usuario usuario = new Usuario();
        usuario.setUsername(validateUsername(usuarioDTO.username()));
        usuario.setPassword(validatePassword(usuarioDTO.password()));
        usuario.setMail(createUserMail(persona));
        usuario.setPersona(persona);
        usuario.setRoles(usuarioDTO.
                roles()
                .stream()
                .map(rolService :: findRolByName)
                .toList());
        usuario.setStatus(Status.ACTIVE);
        Usuario saveUsuario = usuarioRepository.save(usuario);
        return "Registrado exitosamente";
    }

    public Page<CreatedUserResponseDTO> getAllUsers(Pageable pageable, Status status){
        if(status != null){
            Page<Usuario> usuariosPageWhitStatus = usuarioRepository.findByStatus(pageable, status);
            return usuariosPageWhitStatus.map(CreatedUserResponseDTO::new);
        }
        Page<Usuario> usuariosPage = usuarioRepository.findAll(pageable);
        return usuariosPage.map(CreatedUserResponseDTO :: new);
    }

    public CreatedUserResponseDTO getUserById(Long id) {
        try {
            return new CreatedUserResponseDTO(usuarioRepository.getReferenceById(id));
        }catch (EntityNotFoundException ex){
            throw new EntityNotFoundException("El id " + id + " no existe para ningun usuario");
        }
    }
    public void updateUser(UpdateUserDto updateUserDto, Long id) {
        Usuario usuario = findUserById(id);
        usuario.setPassword(validatePassword(updateUserDto.password()));
        usuario.getPersona().setNombres(updateUserDto.nombres());
        usuario.getPersona().setApellidos(updateUserDto.apellidos());
        usuario.getPersona().setIdentificacion(validateIdentificationNumber(updateUserDto.identificacion()));
    }


    public void deactivateUser(Long id) {
        Usuario usuario = findUserById(id);
        usuario.setStatus(Status.DEACTIVATE);
    }

    public JwtTokenDTO LoginUser(LoginUserDTO loginUserDTO, HttpServletResponse httpServletResponse) {
        Optional<Usuario> usuarioOptional = usuarioRepository
                .findByUsernameOrMail(loginUserDTO.username(),
                loginUserDTO.mail());
        if (usuarioOptional.isEmpty()) {
            throw new NoUserFoundException("Credenciales Incorrectas");
        }
        Usuario usuario = usuarioOptional.get();
       if (!passwordEncoder.matches(loginUserDTO.password(), usuario.getPassword())) {
               usuario.setLoginAttempts(usuario.getLoginAttempts() + 1);
               if(usuario.getLoginAttempts() > 3){
                   usuario.setStatus(Status.DEACTIVATE);
                   throw new UserSessionException("Usuario bloqueado de despues de 3 intentos");
               }
               usuarioRepository.save(usuario);

           throw new NoUserFoundException("Credenciales Incorrectas");
       }
       if(usuario.isSessionActive()){
           throw new UserSessionException("Ya tiene una sesion iniciada");
       }
       if(usuario.getLoginAttempts() > 3){
           throw new UserSessionException("Su usuario ha sido bloqueado por muchos intentos fallidos");
       }
        usuario.setSessionActive(true);
        usuario.setLoginAttempts(0);
        sessionService.starSession(usuario, httpServletResponse);
        String token = jwtService.getToken(usuario);
        return new JwtTokenDTO(token);

    }
    public void logout(HttpServletRequest request){
        Usuario usuario = findUserByUsername();
        if(!usuario.isSessionActive()){
            throw new UserSessionException("No tiene sesion iniciada");
        }
        usuario.setSessionActive(false);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    Long sessionId = Long.valueOf(cookie.getValue());
                    sessionService.endSession(sessionId);
                    break;
                }
            }
        }
    }

    private Usuario findUserById(Long id){
        try{
            return usuarioRepository.getReferenceById(id);
        }catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("El id " + id + " no existe para ningun usuario");
        }
    }

    private Usuario findUserByUsername(){
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserDetails> userOptional = usuarioRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found");
        }
        return  (Usuario) userOptional.get();
    }

    private String createUserMail(Persona persona) {
        char firstLetterName = persona.getNombres().charAt(0);
        String firstLastName = persona.getApellidos().split(" ")[0];
        char firstLetterSecondLastName = persona.getApellidos().split(" ")[1].charAt(0);
        String mailBase = (firstLetterName + firstLastName + firstLetterSecondLastName).toLowerCase();
        String mail =mailBase + "@mail.com";
        int counter = 1;
        while (usuarioRepository.findByMail(mail) != null ){
            mail = mailBase + counter + "@mail.com";
            counter++;
        }

        return mail;
    }
   private String validateIdentificationNumber(String identificacion){

           // verifica si tiene 10 dígitos
           if (identificacion.length() != 10) {
               throw new IdentificacionException("La identificacion debe contener 10 digitos");
           }
           // Solo números
           if (!identificacion.matches("[0-9]+")) {
               throw new IdentificacionException("La identificacion debe contener solo digitos");
           }
           // Validar que no tenga seguido 4 veces seguidas un número
           for (int i = 0; i < identificacion.length() - 3; i++) {
               char c = identificacion.charAt(i);
               if (c == identificacion.charAt(i + 1) && c == identificacion.charAt(i + 2) && c == identificacion.charAt(i + 3)) {
                   throw new IdentificacionException("La identificacion solo puede tener 3 digitos iguales seguidos");
               }
           }
           return identificacion;
   }

   private String validateUsername(String username){
       // Longitud máxima de 20 dígitos y mínima de 8 dígitos
       if (username.length() < 8 || username.length() > 20) {
           throw new usernameException("El nombre usuario debe teber 8 caracteres minimo y 20 caracteres maximo ");
       }
       // No contener signos
       if (!username.matches("\\p{Alnum}+")) {
           throw new usernameException("El nombre de usuario no debe contener signos");
       }
       // Al menos un número
       if (!username.matches(".*\\d.*")) {
           throw new usernameException("El nombre de usuario debe contener al menos un numero");
       }
       // Al menos una letra mayúscula
       if (!username.matches(".*[A-Z].*")) {
           throw new usernameException("El nombre de usuario debe contener al menos una letra en mayuscula");
       }
       // No debe estar duplicado
       if (usuarioRepository.existsByUsername(username)) {
           throw new usernameException("Este nombre de usuario ya existe");
       }
       return username;
   }

   private String validatePassword(String password){
        //Debe tener 8 caracteres minimo
       if (password.length() < 8) {
           throw new PasswordException("La contraseña debe contener minimo 8 caracteres");
       }

       // Debe tener al menos una letra mayúscula
       if (!password.matches(".*[A-Z].*")) {
           throw new PasswordException("La contraseña debe contener al menos una letra mayuscula");
       }

       // No debe contener espacios
       if (password.contains(" ")) {
           throw new PasswordException("La contraseña no debe contener espacios");
       }

       // Debe tener al menos un signo
       if (!password.matches(".*\\p{Punct}.*")) {
           throw new PasswordException("La contraseña debe contener al menos un caracter especial");
       }

       return passwordEncoder.encode(password);
   }



}
