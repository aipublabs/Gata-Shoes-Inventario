package com.gatashoes.inventario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desactivamos CSRF para que permita el envío de tu formulario original sin tokens ocultos
                .csrf(csrf -> csrf.disable())

                // 2. Definimos qué URLs están protegidas y cuáles son públicas
                .authorizeHttpRequests(auth -> auth
                        // Acceso totalmente libre para tu pantalla de login y tus estilos/imágenes
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // CUALQUIER otra pantalla interna (como /resumen) requiere inicio de sesión obligatorio
                        .anyRequest().authenticated()
                )

                // NOTA DE INGENIERÍA: NO agregamos .formLogin() para permitir que TU controlador
                // maneje la validación con la base de datos en su propio @PostMapping.
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}