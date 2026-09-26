package sg.edu.ntu.simple_crm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Examines each HTTP request for a bearer token and, when the token is valid,
 * places an authenticated user in Spring Security's current security context.
 *
 * <p>
 * {@link OncePerRequestFilter} ensures this filter runs only once for each
 * request dispatch.
 * </p>
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Constructor injection makes the dependency explicit and easy to test.
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Processes one request before passing control to the next filter.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the remaining servlet filters and eventual controller
     * @throws ServletException if another filter cannot process the request
     * @throws IOException      if request or response handling fails
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Clients normally send JWTs as: Authorization: Bearer <token>.
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // A missing or unsupported authorization scheme is not rejected here.
        // Later security rules decide whether anonymous access is permitted.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove only the scheme prefix, leaving the compact JWT value.
        String token = authHeader.substring("Bearer ".length());

        // Do not create an authenticated identity unless signature and expiry checks
        // pass.
        if (jwtService.isTokenValid(token)) {
            // The JWT subject claim contains the username in this application.
            String username = jwtService.extractUsername(token);

            // TRAINING ONLY: Empty authorities keep the flow simple for learning.
            // In real applications, you would load roles/authorities for this user.
            // The principal is the username; credentials are null because the JWT has
            // already proved the caller's identity.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.emptyList());

            // Attach request metadata such as the remote address and session ID.
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Spring Security reads this context later when authorizing the request.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Always continue so the request can reach authorization filters and
        // controllers.
        filterChain.doFilter(request, response);
    }
}