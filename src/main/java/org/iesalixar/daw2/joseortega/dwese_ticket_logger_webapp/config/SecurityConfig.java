package org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.config;

import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.handlers.CustomOAuth2FailureHandler;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.handlers.CustomOAuth2SuccessHandler;
import org.iesalixar.daw2.joseortega.dwese_ticket_logger_webapp.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configura la seguridad de la aplicación, definiendo autenticación y
 autorización
 * para diferentes roles de usuario, y gestionando la política de sesiones.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Activa la seguridad basada en métodos
public class SecurityConfig {
    private static final Logger logger =
            LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;
/**
 * Configura el filtro de seguridad para las solicitudes HTTP,
 especificando las
 * rutas permitidas y los roles necesarios para acceder a diferentes
 endpoints.
 *
 * @param http instancia de {@link HttpSecurity} para configurar la
seguridad.
 * @return una instancia de {@link SecurityFilterChain} que contiene la
configuración de seguridad.
 * @throws Exception si ocurre un error en la configuración de seguridad.
 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
            Exception {
        logger.info("Entrando en el método securityFilterChain");
// Configuración de seguridad
        http
                .authorizeHttpRequests(auth -> {
                    logger.debug("Configurando autorización de solicitudes HTTP");
                            auth
                                    .requestMatchers("/", "/hello").permitAll()
// Acceso anónimo
                                    .requestMatchers("/admin").hasRole("ADMIN")
// Solo ADMIN
                                    .requestMatchers("/regions", "/provinces",
                                            "/supermarkets", "/locations", "/categories").hasRole("MANAGER")
                            // Solo MANAGER
                                    .requestMatchers("/tickets").hasRole("USER")
// Solo USER
                                    .anyRequest().authenticated();
// Cualquier otra solicitud requiere autenticación
                })
                .formLogin(form -> {
                    logger.debug("Configurando formulario de inicio de sesión");
                    form
                            .loginPage("/login")        //Página personalizada del login
                            .defaultSuccessUrl("/")     //Redirige al inicio despues del login
                            .permitAll();               //permite acceso a la pagina del login a todos los usuarios
                })
                .sessionManagement(session -> {
                    logger.debug("Configurando política de gestión de sesiones");
                            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                            // Usa sesiones cuando sea necesario
                })
                .oauth2Login(oauth2 -> {
                    logger.debug("Configurando login con OAuth2");
                    oauth2
                            .loginPage("/login") // Reutiliza la página de inicio de sesión personalizada
                            .successHandler(customOAuth2SuccessHandler) // Usa el Success Handler personalizado
                            .failureHandler(customOAuth2FailureHandler); // Handler para fallo en autenticación
                });
        logger.info("Saliendo del método securityFilterChain");
        return http.build();
    }

    /**
     * Configura el proveedor de autenticación para usar el servicio de detalles de
     usuario
     * personalizado y el codificador de contraseñas.
     *
     * @return una instancia de {@link DaoAuthenticationProvider} para la
    autenticación.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el codificador de contraseñas para cifrar las contraseñas de
     los usuarios
     * utilizando BCrypt.
     *
     * @return una instancia de {@link PasswordEncoder} que utiliza BCrypt para
    cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Saliendo del método passwordEncoder");
        return encoder;
    }
}