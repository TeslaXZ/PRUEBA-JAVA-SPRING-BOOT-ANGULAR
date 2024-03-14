package com.ec.viamatica.repositories;

import com.ec.viamatica.entities.Usuario;
import com.ec.viamatica.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByMail(String mail);

    boolean existsByUsername(String username);

    Page<Usuario> findByStatus(Pageable pageable, Status status);
    @Query("SELECT u FROM Usuario u WHERE (u.username = ?1 OR u.mail = ?2) AND u.password = ?3")
   Optional<Usuario> findByUsernameOrEmailAndPassword(String username, String email, String password);

    Usuario findByUsernameOrMail(String username, String mail);
}
