package com.adrielbarros.ecommerce.dto;

import com.adrielbarros.ecommerce.model.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long produtoId,
        String nomeProduto,
        BigDecimal precoUnitario,
        Integer quantidade,
        BigDecimal subtotal
) {
    public ItemPedidoResponseDTO(ItemPedido item) {
        this(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getPrecoUnitario(),
                item.getQuantidade(),
                item.getSubtotal()
        );
    }
}