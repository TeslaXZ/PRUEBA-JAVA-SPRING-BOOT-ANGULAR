package com.ec.viamatica.service;

import com.ec.viamatica.entities.Session;
import com.ec.viamatica.entities.Usuario;
import com.ec.viamatica.repositories.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public void starSession(Usuario usuario, HttpServletResponse httpServletResponse){
        Session session = new Session();
        session.setUsuario(usuario);
        session.setFechaIngreso(LocalDateTime.now());
        Session saveSession = sessionRepository.save(session);
        Cookie sessionCookie = new Cookie("sessionId",saveSession.getId().toString());
        httpServletResponse.addCookie(sessionCookie);
    }
    @Transactional
    public void endSession(Long sessionId){
        Session session = sessionRepository.getReferenceById(sessionId);
        session.setFechaEgreso(LocalDateTime.now());
    }
}
