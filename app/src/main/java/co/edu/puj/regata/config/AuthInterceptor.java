package co.edu.puj.regata.config;

import co.edu.puj.regata.domain.entity.Jugador;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        
        // Permitir acceso a recursos estáticos y páginas públicas
        if (requestURI.startsWith("/css/") || 
            requestURI.startsWith("/js/") || 
            requestURI.startsWith("/images/") ||
            requestURI.startsWith("/webjars/") ||
            requestURI.equals("/regata") ||
            requestURI.equals("/regata/") ||
            requestURI.startsWith("/regata/auth/") ||
            requestURI.equals("/favicon.ico")) {
            return true;
        }
        
        // Verificar autenticación
        Long jugadorId = (Long) session.getAttribute("jugadorId");
        Jugador.RolUsuario rol = (Jugador.RolUsuario) session.getAttribute("rol");
        
        if (jugadorId == null || rol == null) {
            response.sendRedirect("/regata/auth/login");
            return false;
        }
        
        // Restricciones por rol según el enunciado
        if (rol == Jugador.RolUsuario.JUGADOR) {
            // Los jugadores solo pueden acceder al juego, no a CRUD
            if (requestURI.startsWith("/regata/juego/")) {
                return true;
            }
            
            // Denegar acceso a funcionalidades CRUD para jugadores
            if (requestURI.startsWith("/regata/jugadores/") ||
                requestURI.startsWith("/regata/barcos/") ||
                requestURI.startsWith("/regata/modelos/") ||
                requestURI.startsWith("/regata/partidas/") ||
                requestURI.startsWith("/regata/mapas/") ||
                requestURI.startsWith("/regata/celdas/")) {
                
                // Permitir solo operaciones de lectura (GET) para algunas entidades
                if (request.getMethod().equals("GET") && 
                    (requestURI.startsWith("/regata/partidas/") || 
                     requestURI.startsWith("/regata/mapas/"))) {
                    return true;
                }
                
                response.sendRedirect("/regata/auth/denied");
                return false;
            }
            
            // Redirigir jugadores que intenten acceder a páginas de administración
            response.sendRedirect("/regata/juego");
            return false;
            
        } else if (rol == Jugador.RolUsuario.ADMIN) {
            // Los administradores no pueden jugar según el enunciado
            if (requestURI.startsWith("/regata/juego/")) {
                response.sendRedirect("/regata/auth/denied");
                return false;
            }
            
            // Permitir acceso a todas las funcionalidades CRUD
            return true;
        }
        
        // Por defecto, denegar acceso
        response.sendRedirect("/regata/auth/denied");
        return false;
    }
}