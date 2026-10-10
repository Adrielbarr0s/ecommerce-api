package com.adrielbarros.ecommerce.dto;

import com.adrielbarros.ecommerce.model.Pedido;
import com.adrielbarros.ecommerce.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime dataCriacao,
        StatusPedido status,
        BigDecimal valorTotal,
        List<ItemPedidoResponseDTO> itens
) {
    public PedidoResponseDTO(Pedido pedido) {
        this(
                pedido.getId(),
                pedido.getDataCriacao(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getItens().stream().map(ItemPedidoResponseDTO::new).toList()
        );
    }
}