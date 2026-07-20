package security.login.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtTokenHelper {

    public String generateToken(UserDetails userDetails) {
        return "Generate token based on UserDetails";
    }
}
