package sg.edu.ntu.simple_crm.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.simple_crm.security.JwtService;

/**
 * Provides authentication endpoints for clients that need a JWT.
 *
 * <p>
 * This controller verifies a username and password through Spring Security.
 * It returns a signed token only after authentication succeeds.
 * </p>
 */
@RestController
// Prefixes every endpoint in this controller with /auth.
@RequestMapping("/auth")
public class AuthController {

    // Delegates credential checking to Spring Security's configured providers.
    private final AuthenticationManager authenticationManager;

    // Creates the JWT returned after successful authentication.
    private final JwtService jwtService;

    // Constructor injection supplies both dependencies and keeps them immutable.
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates login credentials and returns a bearer token.
     *
     * @param request the username and password deserialized from the JSON request
     *                body
     * @return HTTP 200 with a token when authentication succeeds, or HTTP 401 when
     *         it fails
     */
    // Together with the class mapping, this handles POST /auth/login.
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        try {
            // Wrap the submitted credentials in Spring Security's authentication object.
            // authenticate() passes it to the configured authentication provider.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // Reaching this line means no authentication exception was thrown.
            String token = jwtService.generateToken(request.getUsername());

            // ResponseEntity.ok(...) creates an HTTP 200 response with a JSON body.
            return ResponseEntity.ok(new TokenResponse(token));

        } catch (BadCredentialsException e) {
            // Do not reveal whether the username or password was incorrect.
            // An empty 401 response tells the client that authentication failed.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}