package org.example.jwt;

import java.util.Set;

public class Jwks {

    private Set<Jwk> keys;

    public Set<Jwk> getKeys() {
        return keys;
    }

    public void setKeys(Set<Jwk> keys) {
        this.keys = keys;
    }
}
