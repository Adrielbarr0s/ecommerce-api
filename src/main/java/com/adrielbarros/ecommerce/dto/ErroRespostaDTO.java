package com.adrielbarros.ecommerce.dto;

import java.time.LocalDateTime;

public record ErroRespostaDTO(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem,
        String caminho
) {
    public ErroRespostaDTO(Integer status, String erro, String mensagem, String caminho) {
        this(LocalDateTime.now(), status, erro, mensagem, caminho);
    }
}