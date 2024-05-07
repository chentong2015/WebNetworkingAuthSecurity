package org.example.jwt;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

public class JwtAuthenticationService {

	private final String audience;
	private final String issuer;
	
	public JwtAuthenticationService(String audience, String issuer) {
		this.audience = audience;
		this.issuer = issuer;
	}

	// TODO. 验证Request中的请求的Token, 验证Certification签发没有问题
	public void authenticate(HttpServletRequest request){
		final String requestTokenHeader = request.getHeader("Authorization");

		String jwtToken = "";
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
		}
		if (!jwtToken.isEmpty()) {
			System.out.println("Token:" + jwtToken);
			Claims claims = validateJwt(jwtToken);
			String issuer = claims.getIssuer();
			String audience = claims.getAudience();
			if (issuer != null) {
				System.out.println("Find Issue: " + issuer + audience);
			}
		}
	}
	
	private Claims validateJwt(final String accessToken) throws JwtException  {
	    Jws<Claims> claims = Jwts.parser()
	    	.requireAudience(audience)
	    	.requireIssuer(issuer)  
	    	.setSigningKeyResolver(new MySigningKeyResolver())
			.parseClaimsJws(accessToken);
	    return claims.getBody();
	}
}
