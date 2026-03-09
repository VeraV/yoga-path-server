package com.yogapath.security;

public interface TokenService {
    String generateToken(String email, Long userId);
    String getEmailFromToken(String token);
    Long getUserIdFromToken(String token);
    boolean validateToken(String token);
}
