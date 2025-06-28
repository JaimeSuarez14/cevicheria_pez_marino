package com.proyecto.cevicheria_pez_marino.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.proyecto.cevicheria_pez_marino")
public class WebSecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para simplificar el ejemplo
            .authorizeHttpRequests( auth -> auth
                .requestMatchers("/", "/index", "/css/**", "/js/**", "/img/**", "/imagenes/**").permitAll() // Permitir acceso a recursos estáticos y páginas públicas
                .requestMatchers("/usuario/login", "/usuario/registro").permitAll() // Permitir acceso a login y registro
                .requestMatchers("/principal/**", "/carrito/**", "/reserva/**").permitAll() // Permitir acceso a páginas públicas
                .requestMatchers("/administracion/**").hasRole("ADMIN") // Solo administradores pueden acceder
                .anyRequest().authenticated() // Requerir autenticación para cualquier otra solicitud
            )
            .formLogin(form -> form
                .loginPage("/usuario/login") // Página de inicio de sesión personalizada
                .defaultSuccessUrl("/", true) // Redirigir a la página principal después del login
                .permitAll() // Permitir acceso a la página de inicio de sesión
            )    
            
            .logout( logout -> logout
                .logoutUrl("/logout") // URL para cerrar sesión
                .logoutSuccessUrl("/") // Redirigir a la página principal después del logout
                .permitAll()); // Permitir cierre de sesión

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usar BCrypt para codificar contraseñas
    }
}
