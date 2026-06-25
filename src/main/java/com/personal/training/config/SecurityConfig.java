package com.personal.training.config;

import com.personal.training.security.JwtAuthFilter;
import com.personal.training.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UsuarioDetailsService usuarioDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/webjars/**"
                        ).permitAll()

                        // Rotas públicas
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                        // Usuários: só ADMIN gerencia tudo
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN")

                        // Troca de senha: qualquer autenticado
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/*/senha").authenticated()

                        // Personal: ADMIN ou o próprio PERSONAL se cadastrar
                        .requestMatchers(HttpMethod.POST, "/personais").hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(HttpMethod.GET, "/personais").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/personais/**").hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(HttpMethod.PUT, "/personais/**").hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(HttpMethod.DELETE, "/personais/**").hasRole("ADMIN")

                        // Alunos: ADMIN tudo, PERSONAL cadastra/edita/deleta, ALUNO só a si mesmo
                        .requestMatchers(HttpMethod.POST, "/alunos").hasAnyRole("ADMIN", "PERSONAL", "ALUNO")
                        .requestMatchers(HttpMethod.GET, "/alunos").hasAnyRole("ADMIN", "PERSONAL")
                        .requestMatchers(HttpMethod.GET, "/alunos/**").hasAnyRole("ADMIN", "PERSONAL", "ALUNO")
                        .requestMatchers(HttpMethod.PUT, "/alunos/**").hasAnyRole("ADMIN", "PERSONAL", "ALUNO")
                        .requestMatchers(HttpMethod.DELETE, "/alunos/**").hasAnyRole("ADMIN", "PERSONAL")

                        // Treinos
                        .requestMatchers(HttpMethod.POST, "/treinos").hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.GET, "/treinos").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/treinos/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.PUT, "/treinos/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.DELETE, "/treinos/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        // Exercícios

                        .requestMatchers(HttpMethod.POST, "/exercicios")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.GET, "/exercicios")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.GET, "/exercicios/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.PUT, "/exercicios/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.DELETE, "/exercicios/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        // Itens do treino

                        .requestMatchers(HttpMethod.POST, "/itens-treino")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.GET, "/itens-treino")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.GET, "/itens-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.PUT, "/itens-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        .requestMatchers(HttpMethod.DELETE, "/itens-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL")

                        // Execucao Treino

                        .requestMatchers(HttpMethod.POST, "/execucoes-treino")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.GET, "/execucoes-treino")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.GET, "/execucoes-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.PUT, "/execucoes-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")

                        .requestMatchers(HttpMethod.DELETE, "/execucoes-treino/**")
                        .hasAnyRole("ADMIN", "PERSONAL", "ALUNO")
                        .anyRequest().authenticated()



                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}