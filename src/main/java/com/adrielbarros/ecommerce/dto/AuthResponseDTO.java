package com.adrielbarros.ecommerce.dto;

public record AuthResponseDTO(
        String token,
        String tipo,
        String email,
        String nome
) {
    public AuthResponseDTO(String token, String email, String nome) {
        this(token, "Bearer", email, nome);
    }
}