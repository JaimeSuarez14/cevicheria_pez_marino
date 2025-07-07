package com.proyecto.cevicheria_pez_marino.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    private RequestCache requestCache = new HttpSessionRequestCache(); // Necesitamos un RequestCache

    public CustomAuthenticationSuccessHandler() {
        // Configura la URL por defecto en el constructor
        setDefaultTargetUrl("/principal/menu");
        setTargetUrlParameter("continue");
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        // 1. Verificar si hay un parámetro 'continue' en la URL del login
        String continueUrlFromParam = request.getParameter("continue");

        // 2. Verificar si hay una URL guardada en la sesión (SavedRequest)
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String savedRequestUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : null;

        // Definir la URL de destino preferida
        String targetUrl = null;

        // Prioridad 1: Parámetro 'continue' (si existe y no es la página de registro)
        if (continueUrlFromParam != null && !continueUrlFromParam.isEmpty() && !"/usuario/registro".equals(continueUrlFromParam)) {
            targetUrl = continueUrlFromParam;
        }
        // Prioridad 2: URL Guardada (si existe y no es la página de registro)
        else if (savedRequestUrl != null && !"/usuario/registro".equals(savedRequestUrl)) {
            targetUrl = savedRequestUrl;
            requestCache.removeRequest(request, response); // Limpiar la SavedRequest una vez usada
        }

        // Si se encontró una URL de destino válida (no la página de registro), redirigir a ella
        if (targetUrl != null) {
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // Si la URL previa era la página de registro, o no había ninguna URL previa (acceso directo al login),
            // entonces redirigir a la defaultTargetUrl
            getRedirectStrategy().sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
