package com.proyecto.cevicheria_pez_marino.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class CommonServiceImplen implements CommonService {

    @SuppressWarnings("null")
    public void removerMensajeSession(){
       HttpServletRequest request= ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
       HttpSession session = request.getSession();
       session.removeAttribute("conformidadMensaje");
       session.removeAttribute("errorMensaje");
    }
}
