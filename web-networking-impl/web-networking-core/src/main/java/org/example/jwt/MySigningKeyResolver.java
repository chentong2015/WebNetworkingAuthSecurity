package org.example.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class MySigningKeyResolver implements SigningKeyResolver {

    private RestTemplate restTemplate;
    private final String endpoint = "https://xxx.com";

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        return resolveSigningKey(header);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String arg1) {
        return resolveSigningKey(header);
    }

    private Key resolveSigningKey(JwsHeader header) {
        return getSigningCertificate(getX5t(header)).getPublicKey();
    }

    private String getX5t(JwsHeader header) {
        return header.get("x5t").toString();
    }

    public X509Certificate getSigningCertificate(String keyId) {
        Jwks jwks = restTemplate.getForEntity(endpoint, Jwks.class).getBody();
        String x509Base64 = null;
        for (Jwk jwk : jwks.getKeys()) {
            String kid = jwk.getKid();
            if (keyId.equals(jwk.getKid())) {
                x509Base64 = jwk.getX5c().get(0);
            }
        }

        byte[] x509bytes = Base64.getDecoder().decode(x509Base64);
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(x509bytes));
        } catch (CertificateException e) {
            return null;
        }
    }
}
